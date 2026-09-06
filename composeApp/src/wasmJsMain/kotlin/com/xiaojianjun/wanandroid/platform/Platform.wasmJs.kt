package com.xiaojianjun.wanandroid.platform

import com.xiaojianjun.wanandroid.common.core.ImageCache
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.js.Js
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@JsFun("(key) => window.localStorage.getItem(key)")
private external fun readPreference(key: String): String?
@JsFun("(key, value) => window.localStorage.setItem(key, value)")
private external fun writePreference(key: String, value: String)
@JsFun("(prefix) => Object.keys(window.localStorage).filter(k => k.startsWith(prefix)).forEach(k => window.localStorage.removeItem(k))")
private external fun clearPreferences(prefix: String)
@JsFun("() => Date.now()")
private external fun now(): Double
@JsFun("(url) => window.location.origin + '/image?url=' + encodeURIComponent(url)")
private external fun proxyImage(url: String): String
@JsFun("""(time, pattern) => {
    const d = new Date(time), p = n => String(n).padStart(2, '0');
    return pattern.replace(/YYYY|yyyy/g, String(d.getFullYear())).replace(/MM/g,p(d.getMonth()+1))
      .replace(/dd/g,p(d.getDate())).replace(/HH/g,p(d.getHours())).replace(/mm/g,p(d.getMinutes())).replace(/ss/g,p(d.getSeconds()));
}""")
private external fun dateText(time: Double, pattern: String): String

actual object PlatformPreferences {
    private fun key(namespace: String, key: String) = "wanandroid.$namespace.$key"
    actual fun getString(namespace: String, key: String, default: String) = readPreference(key(namespace, key)) ?: default
    actual fun putString(namespace: String, key: String, value: String) = writePreference(key(namespace, key), value)
    actual fun getInt(namespace: String, key: String, default: Int) = getString(namespace, key, "").toIntOrNull() ?: default
    actual fun putInt(namespace: String, key: String, value: Int) = putString(namespace, key, value.toString())
    actual fun getBoolean(namespace: String, key: String, default: Boolean) = getString(namespace, key, "").toBooleanStrictOrNull() ?: default
    actual fun putBoolean(namespace: String, key: String, value: Boolean) = putString(namespace, key, value.toString())
    actual fun clear(namespace: String) = clearPreferences("wanandroid.$namespace.")
}

actual object Platform {
    actual val apiBaseUrl = "/api"
    actual val managesCookies = true
    actual val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
    actual val versionName = "1.0.6"
    actual fun nowMillis() = now().toLong()
    actual fun formatDate(time: Long, pattern: String) = dateText(time.toDouble(), pattern)
    actual fun imageUrl(url: String) = proxyImage(url)
    actual suspend fun cacheSize() = ImageCache.size()
    actual suspend fun clearCache() = ImageCache.clear()
}

actual fun createPlatformHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Js) { config() }
