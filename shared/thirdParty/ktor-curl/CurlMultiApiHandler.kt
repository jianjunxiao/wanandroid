/*
 * Copyright 2014-2025 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

// 本文件来自 CPF Ktor 3.3.3-1.0.0；本地修复范围和升级方式见同目录 README.md。
package io.ktor.client.engine.curl.internal

import io.ktor.client.engine.*
import io.ktor.client.plugins.*
import io.ktor.utils.io.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.locks.*
import kotlinx.cinterop.*
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.DisposableHandle
import kotlinx.io.readByteArray
import libcurl.*
import platform.posix.getenv

/** 持有请求的全部 native 回调引用，并在正常完成、取消或关闭时统一释放。 */
@OptIn(ExperimentalForeignApi::class)
private class RequestHolder(
    val responseCompletable: CompletableDeferred<CurlSuccess>,
    val requestWrapper: StableRef<CurlRequestBodyData>,
    val responseWrapper: StableRef<CurlResponseBodyData>,
    val responseBuilder: StableRef<CurlResponseBuilder>,
) {
    // 取消监听必须覆盖响应体传输，不能在响应头交付时提前注销。
    var cancellationRegistration: DisposableHandle? = null

    /** 在 easy handle 移除之后调用一次，避免请求头或 CURLOPT_PRIVATE 引用在取消分支遗留。 */
    fun dispose() {
        cancellationRegistration?.dispose()
        curl_slist_free_all(responseBuilder.get().request.headers)
        responseBuilder.dispose()
        requestWrapper.dispose()
        responseWrapper.dispose()
    }
}

@OptIn(InternalAPI::class, ExperimentalForeignApi::class)
internal class CurlMultiApiHandler : Closeable {
    private val activeHandles = mutableMapOf<EasyHandle, RequestHolder>()
    private val cancelledHandles = mutableSetOf<Pair<EasyHandle, Throwable>>()

    private val multiHandle: MultiHandle = curl_multi_init()
        ?: throw RuntimeException("Could not initialize curl multi handle")

    private val easyHandlesToUnpauseLock = SynchronizedObject()
    private val easyHandlesToUnpause = mutableListOf<EasyHandle>()

    override fun close() {
        if (activeHandles.isNotEmpty()) handleCompleted()
        for ((handle, holder) in activeHandles) {
            cleanupEasyHandle(handle)
            holder.dispose()
        }

        activeHandles.clear()
        curl_multi_cleanup(multiHandle).verify()
    }

    /** 创建 Curl 请求并登记回调资源；完成结果经 deferred 交回调用方，资源统一由 RequestHolder 释放。 */
    fun scheduleRequest(request: CurlRequestData, deferred: CompletableDeferred<CurlSuccess>): EasyHandle {
        val easyHandle = curl_easy_init()
            ?: error("Could not initialize an easy handle")

        val bodyStartedReceiving = CompletableDeferred<Unit>()
        val responseBody = if (request.isUpgradeRequest) {
            CurlWebSocketResponseBody(easyHandle)
        } else {
            CurlHttpResponseBody(request.executionContext) {
                unpauseEasyHandle(easyHandle)
            }
        }
        val responseData = CurlResponseBuilder(request, bodyStartedReceiving, responseBody)
        val responseDataRef = responseData.asStablePointer()
        val responseWrapper = responseBody.asStablePointer()

        bodyStartedReceiving.invokeOnCompletion {
            val result = collectSuccessResponse(easyHandle) ?: return@invokeOnCompletion
            activeHandles[easyHandle]!!.responseCompletable.complete(result)
        }

        setupMethod(easyHandle, request.method, request.contentLength)
        val requestWrapper = setupUploadContent(easyHandle, request)
        val requestHolder = RequestHolder(
            deferred,
            requestWrapper.asStableRef(),
            responseWrapper.asStableRef(),
            responseDataRef.asStableRef()
        )

        activeHandles[easyHandle] = requestHolder

        easyHandle.apply {
            option(CURLOPT_URL, request.url)
            option(CURLOPT_HTTPHEADER, request.headers)
            option(CURLOPT_HEADERFUNCTION, staticCFunction(::onHeadersReceived))
            option(CURLOPT_HEADERDATA, responseDataRef)
            option(CURLOPT_WRITEFUNCTION, staticCFunction(::onBodyChunkReceived))
            option(CURLOPT_WRITEDATA, responseWrapper)
            option(CURLOPT_PRIVATE, responseDataRef)
            option(CURLOPT_ACCEPT_ENCODING, "")
            request.connectTimeout?.let {
                if (it != HttpTimeoutConfig.INFINITE_TIMEOUT_MS) {
                    option(CURLOPT_CONNECTTIMEOUT_MS, request.connectTimeout)
                } else {
                    option(CURLOPT_CONNECTTIMEOUT_MS, Long.MAX_VALUE)
                }
            }

            request.proxy?.let { proxy ->
                option(CURLOPT_PROXY, fixProxyUrl(proxy.toString(), proxy.type))
                option(CURLOPT_SUPPRESS_CONNECT_HEADERS, 1L)
                if (request.forceProxyTunneling) {
                    option(CURLOPT_HTTPPROXYTUNNEL, 1L)
                }
            }

            if (!request.sslVerify) {
                option(CURLOPT_SSL_VERIFYPEER, 0L)
                option(CURLOPT_SSL_VERIFYHOST, 0L)
            }
            request.caPath?.let { option(CURLOPT_CAPATH, it) }
            request.caInfo?.let { option(CURLOPT_CAINFO, it) }
        }

        curl_multi_add_handle(multiHandle, easyHandle).verify()

        return easyHandle
    }

    /**
     * 将请求取消监听交给传输资源统一管理，覆盖等待响应头和读取响应体两个阶段。
     * 必须在 Curl 专属线程中于 scheduleRequest 后立即调用，早于下一次 perform。
     */
    internal fun attachCancellationHandler(easyHandle: EasyHandle, registration: DisposableHandle) {
        activeHandles.getValue(easyHandle).cancellationRegistration = registration
    }

    private fun fixProxyUrl(url: String, proxyType: ProxyType): String {
        return if (proxyType == ProxyType.SOCKS) url.replaceFirst("socks://", "socks5://") else url
    }

    /**
     * 在 Curl 专属线程登记取消，由 handleCompleted 统一关闭响应、移除登记并释放句柄。
     * 已完成的请求直接忽略，避免对已释放句柄再次操作；不能在保留 activeHandles 登记时直接释放。
     *
     * @param easyHandle 要取消的请求句柄。
     * @param cause 传递给等待请求和响应通道的取消原因。
     * @param requestCompletion 原请求的身份标记，避免 native 地址复用后误取消另一条请求。
     */
    internal fun cancelRequest(
        easyHandle: EasyHandle,
        cause: Throwable,
        requestCompletion: CompletableDeferred<CurlSuccess>
    ) {
        if (activeHandles[easyHandle]?.responseCompletable === requestCompletion) {
            cancelledHandles += Pair(easyHandle, cause)
        }
    }

    /** 在 Curl 专属线程先清理取消队列，再推进传输；不等待远端响应才释放已取消的连接。 */
    internal fun perform(transfersRunning: IntVarOf<Int>) {
        if (cancelledHandles.isNotEmpty()) handleCompleted()
        if (activeHandles.isEmpty()) return

        synchronized(easyHandlesToUnpauseLock) {
            var handle = easyHandlesToUnpause.removeFirstOrNull()
            while (handle != null) {
                // 延迟的背压恢复可能晚于取消，已经完成的句柄不能再次使用。
                if (handle in activeHandles) curl_easy_pause(handle, CURLPAUSE_CONT)
                handle = easyHandlesToUnpause.removeFirstOrNull()
            }
        }
        curl_multi_perform(multiHandle, transfersRunning.ptr).verify()
        if (transfersRunning.value != 0) {
            curl_multi_poll(multiHandle, null, 0.toUInt(), pollTimeout, null).verify()
        }
        if (transfersRunning.value < activeHandles.size) {
            handleCompleted()
        }
    }

    internal fun hasHandlers(): Boolean = activeHandles.isNotEmpty()

    private fun setupMethod(
        easyHandle: EasyHandle,
        method: String,
        size: Long
    ) {
        easyHandle.apply {
            when (method) {
                "GET" -> option(CURLOPT_HTTPGET, 1L)

                "PUT" -> {
                    option(CURLOPT_PUT, 1L)
                    option(CURLOPT_INFILESIZE_LARGE, size)
                }

                "POST" -> {
                    option(CURLOPT_POST, 1L)
                    option(CURLOPT_POSTFIELDSIZE_LARGE, size)
                }

                "HEAD" -> option(CURLOPT_NOBODY, 1L)

                else -> {
                    if (size > 0) {
                        option(CURLOPT_POST, 1L)
                        option(CURLOPT_POSTFIELDSIZE_LARGE, size)
                    }
                    option(CURLOPT_CUSTOMREQUEST, method)
                }
            }
        }
    }

    private fun setupUploadContent(easyHandle: EasyHandle, request: CurlRequestData): COpaquePointer {
        val requestPointer = CurlRequestBodyData(
            body = request.content,
            callContext = request.executionContext,
            onUnpause = {
                unpauseEasyHandle(easyHandle)
            }
        ).asStablePointer()

        easyHandle.apply {
            option(CURLOPT_READDATA, requestPointer)
            option(CURLOPT_READFUNCTION, staticCFunction(::onBodyChunkRequested))
        }
        return requestPointer
    }

    private fun handleCompleted() {
        for (cancellation in cancelledHandles) {
            val cancelled = processCancelledEasyHandle(cancellation.first, cancellation.second)
            val handler = activeHandles.remove(cancellation.first)!!
            handler.responseCompletable.completeExceptionally(cancelled.cause)
            handler.dispose()
        }
        cancelledHandles.clear()

        memScoped {
            do {
                val messagesLeft = alloc<IntVar>()
                val messagePtr = curl_multi_info_read(multiHandle, messagesLeft.ptr)
                val message = messagePtr?.pointed ?: continue

                val easyHandle = message.easy_handle
                    ?: error("Got a null easy handle from the message")

                try {
                    val result = processCompletedEasyHandle(message.msg, easyHandle, message.data.result)
                    val deferred = activeHandles[easyHandle]!!.responseCompletable
                    if (deferred.isCompleted) {
                        // already completed with partial response
                        continue
                    }
                    when (result) {
                        is CurlSuccess -> deferred.complete(result)
                        is CurlFail -> deferred.completeExceptionally(result.cause)
                    }
                } finally {
                    activeHandles.remove(easyHandle)!!.dispose()
                }
            } while (messagesLeft.value != 0)
        }
    }

    private fun processCancelledEasyHandle(easyHandle: EasyHandle, cause: Throwable): CurlFail = memScoped {
        try {
            val responseDataRef = alloc<COpaquePointerVar>()
            easyHandle.apply { getInfo(CURLINFO_PRIVATE, responseDataRef.ptr) }
            val responseBuilder = responseDataRef.value!!.fromCPointer<CurlResponseBuilder>()
            try {
                return CurlFail(cause)
            } finally {
                responseBuilder.responseBody.close(cause)
                responseBuilder.headersBytes.close()
            }
        } finally {
            cleanupEasyHandle(easyHandle)
        }
    }

    private fun processCompletedEasyHandle(
        message: CURLMSG?,
        easyHandle: EasyHandle,
        result: CURLcode
    ): CurlResponseData = memScoped {
        try {
            val responseDataRef = alloc<COpaquePointerVar>()
            val httpStatusCode = alloc<LongVar>()

            easyHandle.apply {
                getInfo(CURLINFO_RESPONSE_CODE, httpStatusCode.ptr)
                getInfo(CURLINFO_PRIVATE, responseDataRef.ptr)
            }

            val responseBuilder = responseDataRef.value!!.fromCPointer<CurlResponseBuilder>()
            try {
                collectFailedResponse(message, responseBuilder.request, result, httpStatusCode.value)
                    ?: collectSuccessResponse(easyHandle)!!
            } finally {
                responseBuilder.responseBody.close()
                responseBuilder.headersBytes.close()
            }
        } finally {
            cleanupEasyHandle(easyHandle)
        }
    }

    private fun collectFailedResponse(
        message: CURLMSG?,
        request: CurlRequestData,
        result: CURLcode,
        httpStatusCode: Long
    ): CurlFail? {
        // 请求头由 RequestHolder 在 handle 清理后统一释放，成功、失败和取消共用同一释放路径。
        if (message != CURLMSG.CURLMSG_DONE) {
            return CurlFail(
                IllegalStateException("Request $request failed: $message")
            )
        }

        if (httpStatusCode != 0L) {
            return null
        }

        if (result == CURLE_OPERATION_TIMEDOUT) {
            return CurlFail(ConnectTimeoutException(request.url, request.connectTimeout))
        }

        val errorMessage = curl_easy_strerror(result)?.toKStringFromUtf8()

        if (result == CURLE_PEER_FAILED_VERIFICATION) {
            return CurlFail(
                IllegalStateException(
                    "TLS verification failed for request: $request. Reason: $errorMessage"
                )
            )
        }

        return CurlFail(
            IllegalStateException("Connection failed for request: $request. Reason: $errorMessage")
        )
    }

    /** 从当前传输读取状态码、协商后的 HTTP 版本和响应头；尚未收到状态码时交由完成分支处理。 */
    private fun collectSuccessResponse(easyHandle: EasyHandle): CurlSuccess? = memScoped {
        val responseDataRef = alloc<COpaquePointerVar>()
        val httpProtocolVersion = alloc<LongVar>()
        val httpStatusCode = alloc<LongVar>()

        easyHandle.apply {
            getInfo(CURLINFO_RESPONSE_CODE, httpStatusCode.ptr)
            getInfo(CURLINFO_HTTP_VERSION, httpProtocolVersion.ptr)
            getInfo(CURLINFO_PRIVATE, responseDataRef.ptr)
        }

        if (httpStatusCode.value == 0L) {
            // if error happened, it will be handled in collectCompleted
            return@memScoped null
        }

        val responseBuilder = responseDataRef.value!!.fromCPointer<CurlResponseBuilder>()
        with(responseBuilder) {
            val headers = headersBytes.build().readByteArray()

            CurlSuccess(
                httpStatusCode.value.toInt(),
                httpProtocolVersion.value.toUInt(),
                headers,
                responseBody
            )
        }
    }

    fun wakeup() {
        curl_multi_wakeup(multiHandle)
    }

    private fun unpauseEasyHandle(easyHandle: EasyHandle) {
        synchronized(easyHandlesToUnpauseLock) {
            easyHandlesToUnpause.add(easyHandle)
        }
        curl_multi_wakeup(multiHandle)
    }

    private fun cleanupEasyHandle(easyHandle: EasyHandle) {
        curl_multi_remove_handle(multiHandle, easyHandle).verify()
        curl_easy_cleanup(easyHandle)
    }

    private companion object {
        private const val DEFAULT_POLL_TIMEOUT_MS = 100
        val pollTimeout by lazy { getenv("KTOR_CURL_POLL_TIMEOUT")?.toKString()?.toInt() ?: DEFAULT_POLL_TIMEOUT_MS }
    }
}
