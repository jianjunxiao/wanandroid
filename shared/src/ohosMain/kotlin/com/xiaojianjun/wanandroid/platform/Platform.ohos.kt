@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)

package com.xiaojianjun.wanandroid.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.ArkUIView2
import androidx.compose.ui.napi.js
import com.xiaojianjun.wanandroid.common.core.ImageCache
import com.xiaojianjun.wanandroid.common.core.JsonCodec
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import kotlinx.atomicfu.locks.SynchronizedObject
import kotlinx.atomicfu.locks.synchronized
import kotlinx.cinterop.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.posix.*

object HarmonyPlatform {
    internal lateinit var filesDir: String
    internal lateinit var cacheDir: String
    fun initialize(filesDir: String, cacheDir: String) {
        this.filesDir = filesDir
        this.cacheDir = cacheDir
    }
}

actual object PlatformPreferences {
    private val lock = SynchronizedObject()
    private fun file(namespace: String) = "${HarmonyPlatform.filesDir}/wanandroid_$namespace.json"
    private fun read(namespace: String): MutableMap<String, String> {
        val handle = fopen(file(namespace), "rb") ?: return mutableMapOf()
        return try {
            check(fseek(handle, 0, SEEK_END) == 0)
            val size = ftell(handle)
            check(size in 0..50_000_000) { "本地数据大小无效" }
            rewind(handle)
            val bytes = ByteArray(size.toInt())
            if (bytes.isNotEmpty()) bytes.usePinned { check(fread(it.addressOf(0), 1u, size.toULong(), handle) == size.toULong()) }
            JsonCodec.fromJson<MutableMap<String, String>>(bytes.decodeToString()) ?: mutableMapOf()
        } finally { fclose(handle) }
    }
    private fun write(namespace: String, values: Map<String, String>) {
        val target = file(namespace)
        val temporary = "$target.tmp"
        val handle = fopen(temporary, "wb") ?: error("无法写入本地数据")
        try {
            val bytes = JsonCodec.toJson(values).encodeToByteArray()
            bytes.usePinned { check(fwrite(it.addressOf(0), 1u, bytes.size.toULong(), handle) == bytes.size.toULong()) }
            check(fflush(handle) == 0)
        } finally { fclose(handle) }
        check(rename(temporary, target) == 0) { "保存本地数据失败" }
    }
    actual fun getString(namespace: String, key: String, default: String): String = synchronized(lock) { read(namespace)[key] ?: default }
    actual fun putString(namespace: String, key: String, value: String) = synchronized(lock) {
        write(namespace, read(namespace).apply { put(key, value) })
    }
    actual fun getInt(namespace: String, key: String, default: Int) = getString(namespace, key, "").toIntOrNull() ?: default
    actual fun putInt(namespace: String, key: String, value: Int) = putString(namespace, key, value.toString())
    actual fun getBoolean(namespace: String, key: String, default: Boolean) = getString(namespace, key, "").toBooleanStrictOrNull() ?: default
    actual fun putBoolean(namespace: String, key: String, value: Boolean) = putString(namespace, key, value.toString())
    actual fun clear(namespace: String) = synchronized(lock) { write(namespace, emptyMap()) }
}

actual object Platform {
    actual val apiBaseUrl = "https://wanandroid.com"
    actual val managesCookies = false
    actual val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
    actual val versionName = "1.0.6"
    actual fun nowMillis(): Long = memScoped {
        val now = alloc<timeval>()
        gettimeofday(now.ptr, null)
        now.tv_sec * 1000 + now.tv_usec / 1000
    }
    actual fun formatDate(time: Long, pattern: String): String = memScoped {
        val seconds = alloc<time_tVar> { value = time / 1000 }
        val local = alloc<tm>()
        localtime_r(seconds.ptr, local.ptr)
        val format = pattern.replace("YYYY", "%Y").replace("yyyy", "%Y").replace("MM", "%m")
            .replace("dd", "%d").replace("HH", "%H").replace("mm", "%M").replace("ss", "%S")
        val buffer = allocArray<ByteVar>(128)
        strftime(buffer, 128u, format, local.ptr)
        buffer.toKString()
    }
    actual fun imageUrl(url: String) = url
    actual suspend fun cacheSize() = withContext(Dispatchers.Default) { ImageCache.size() }
    actual suspend fun clearCache() = withContext(Dispatchers.Default) { ImageCache.clear() }
}

actual fun createPlatformHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(HarmonyHttpEngine()) { config() }

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    androidx.compose.ui.backhandler.BackHandler(enabled, onBack)
}

@Composable
actual fun ArticleWebView(url: String, textZoom: Int, modifier: Modifier) {
    key(url, textZoom) {
        ArkUIView2(
            name = "WanArticleWeb",
            modifier = modifier,
            parameter = js { "url"(url); "textZoom"(textZoom) },
        )
    }
}
