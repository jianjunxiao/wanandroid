/*
 * Copyright 2014-2019 JetBrains s.r.o and contributors. Use of this source code is governed by the Apache 2.0 license.
 */

// 本文件来自 CPF Ktor 3.3.3-1.0.0；本地修复范围和升级方式见同目录 README.md。
package io.ktor.client.engine.curl

import io.ktor.client.engine.curl.internal.*
import io.ktor.util.*
import io.ktor.utils.io.*
import kotlinx.atomicfu.atomic
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.IntVar
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlin.coroutines.CoroutineContext

internal class RequestContainer(
    val requestData: CurlRequestData,
    val completionHandler: CompletableDeferred<CurlSuccess>
)

internal class CurlProcessor(coroutineContext: CoroutineContext) {
    @OptIn(InternalAPI::class)
    private val curlDispatcher: CloseableCoroutineDispatcher =
        Dispatchers.createFixedThreadDispatcher("curl-dispatcher", 1)

    private var curlApi: CurlMultiApiHandler? by atomic(null)
    private val closed = atomic(false)

    private val curlScope = CoroutineScope(coroutineContext + curlDispatcher)
    private val requestQueue: Channel<RequestContainer> = Channel(Channel.UNLIMITED)

    init {
        val init = curlScope.launch {
            curlApi = CurlMultiApiHandler()
        }

        runBlocking {
            init.join()
        }

        runEventLoop()
    }

    suspend fun executeRequest(request: CurlRequestData): CurlSuccess {
        val result = CompletableDeferred<CurlSuccess>()
        requestQueue.send(RequestContainer(request, result))
        curlApi!!.wakeup()
        return result.await()
    }

    /** 在 Curl 专属线程串行处理请求，并在每轮之后让出线程，使同一线程的取消任务能够执行。 */
    @OptIn(DelicateCoroutinesApi::class, ExperimentalForeignApi::class)
    private fun runEventLoop() {
        curlScope.launch {
            memScoped {
                val transfersRunning = alloc<IntVar>()
                val api = curlApi!!
                while (!requestQueue.isClosedForReceive) {
                    drainRequestQueue(api)
                    api.perform(transfersRunning)
                    // 存在挂起连接时 tryReceive 和 native poll 都不会挂起协程，必须主动调度取消任务。
                    yield()
                }
            }
        }
    }

    /** 登记排队请求，并让取消监听存活到完整传输结束；响应头交付不代表响应体已读取完毕。 */
    @OptIn(ExperimentalForeignApi::class)
    private suspend fun drainRequestQueue(api: CurlMultiApiHandler) {
        while (true) {
            val container = if (api.hasHandlers()) {
                requestQueue.tryReceive()
            } else {
                requestQueue.receiveCatching()
            }.getOrNull() ?: break

            val requestHandler = api.scheduleRequest(container.requestData, container.completionHandler)

            val requestCleaner = container.requestData.executionContext.invokeOnCompletion { cause ->
                if (cause == null) return@invokeOnCompletion
                cancelRequest(requestHandler, container.completionHandler, cause)
            }

            api.attachCancellationHandler(requestHandler, requestCleaner)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun close() {
        if (!closed.compareAndSet(false, true)) return

        requestQueue.close()
        curlApi!!.wakeup()

        GlobalScope.launch(curlDispatcher) {
            curlScope.coroutineContext[Job]!!.join()
            curlApi!!.close()
        }.invokeOnCompletion {
            curlDispatcher.close()
        }
    }

    /** 将任意线程发起的取消交回 Curl 线程，并携带原请求身份，防止延迟回调误操作复用的句柄。 */
    @OptIn(ExperimentalForeignApi::class)
    private fun cancelRequest(
        easyHandle: EasyHandle,
        requestCompletion: CompletableDeferred<CurlSuccess>,
        cause: Throwable
    ) {
        curlScope.launch {
            curlApi!!.cancelRequest(easyHandle, cause, requestCompletion)
        }
    }
}
