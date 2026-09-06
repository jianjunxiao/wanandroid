@file:OptIn(io.ktor.utils.io.InternalAPI::class, kotlinx.cinterop.ExperimentalForeignApi::class, kotlin.experimental.ExperimentalNativeApi::class)

package com.xiaojianjun.wanandroid.platform

import androidx.compose.ui.napi.nApiValue
import com.xiaojianjun.wanandroid.common.core.JsonCodec
import io.ktor.client.engine.HttpClientEngineBase
import io.ktor.client.engine.HttpClientEngineCapability
import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.callContext
import io.ktor.client.engine.mergeHeaders
import io.ktor.client.plugins.HttpTimeoutCapability
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.Headers
import io.ktor.http.HttpProtocolVersion
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.OutgoingContent
import io.ktor.util.date.GMTDate
import io.ktor.utils.io.ByteReadChannel
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.io.encoding.Base64
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable

@Serializable
private data class NativeHttpRequest(
    val url: String,
    val method: String,
    val headers: Map<String, String>,
    val body: String,
)

@Serializable
private data class NativeHttpResponse(
    val status: Int = 0,
    val headers: Map<String, List<String>> = emptyMap(),
    val body: String = "",
    val error: String? = null,
)

/** 使用 NetworkKit 执行 HTTPS；Ktor 保留表单、Cookie、超时与错误处理。 */
internal class HarmonyHttpEngine : HttpClientEngineBase("wanandroid-networkkit") {
    override val config = HttpClientEngineConfig()
    override val supportedCapabilities: Set<HttpClientEngineCapability<out Any>> = setOf(HttpTimeoutCapability)

    override suspend fun execute(data: HttpRequestData): HttpResponseData {
        val started = GMTDate()
        val headers = mutableMapOf<String, String>()
        mergeHeaders(data.headers, data.body) { name, value -> headers[name] = value }
        val bytes = when (val body = data.body) {
            is OutgoingContent.ByteArrayContent -> body.bytes()
            is OutgoingContent.NoContent -> byteArrayOf()
            else -> error("不支持的请求体类型: ${body::class.simpleName}")
        }
        val response = HarmonyHttpRequests.execute(NativeHttpRequest(
            data.url.toString(), data.method.value, headers, Base64.encode(bytes),
        ))
        response.error?.let { throw IllegalStateException(it) }
        return HttpResponseData(
            HttpStatusCode.fromValue(response.status),
            started,
            Headers.build { response.headers.forEach { (name, values) -> appendAll(name, values) } },
            HttpProtocolVersion.HTTP_1_1,
            ByteReadChannel(Base64.decode(response.body)),
            callContext(),
        )
    }
}

private object HarmonyHttpRequests {
    // 请求、回调和取消都切回 ArkTS 所属主线程，避免跨线程使用 N-API。
    private var nextId = 0
    private val pending = mutableMapOf<Int, CancellableContinuation<NativeHttpResponse>>()
    private val cleanupScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    suspend fun execute(request: NativeHttpRequest): NativeHttpResponse = withContext(Dispatchers.Main.immediate) {
        suspendCancellableCoroutine { continuation ->
            val id = ++nextId
            pending[id] = continuation
            continuation.invokeOnCancellation {
                cleanupScope.launch {
                    if (pending.remove(id) != null) HarmonyHost.bridge.call("cancelRequest", id.nApiValue())
                }
            }
            try {
                HarmonyHost.bridge.call("request", id.nApiValue(), JsonCodec.toJson(request).nApiValue())
            } catch (error: Exception) {
                pending.remove(id)
                continuation.resumeWithException(error)
            }
        }
    }

    fun complete(id: Int, json: String) {
        val continuation = pending.remove(id) ?: return
        try {
            continuation.resume(JsonCodec.json.decodeFromString<NativeHttpResponse>(json))
        } catch (error: Exception) {
            continuation.resumeWithException(error)
        }
    }
}

@CName("WanCompleteHttp")
fun WanCompleteHttp(id: Int, response: CPointer<ByteVar>) {
    HarmonyHttpRequests.complete(id, response.toKString())
}
