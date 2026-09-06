package com.xiaojianjun.wanandroid.platform

import android.content.Context
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.viewinterop.AndroidView
import coil3.SingletonImageLoader
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import java.text.SimpleDateFormat
import java.util.Locale
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AndroidPlatform {
    internal lateinit var context: Context
    internal var version: String = "1.0.6"
    fun initialize(context: Context, versionName: String) {
        this.context = context.applicationContext
        version = versionName
    }
}

actual object PlatformPreferences {
    private fun prefs(namespace: String) = AndroidPlatform.context.getSharedPreferences(namespace, Context.MODE_PRIVATE)
    actual fun getString(namespace: String, key: String, default: String) = prefs(namespace).getString(key, default) ?: default
    actual fun putString(namespace: String, key: String, value: String) { prefs(namespace).edit().putString(key, value).apply() }
    actual fun getInt(namespace: String, key: String, default: Int) = prefs(namespace).getInt(key, default)
    actual fun putInt(namespace: String, key: String, value: Int) { prefs(namespace).edit().putInt(key, value).apply() }
    actual fun getBoolean(namespace: String, key: String, default: Boolean) = prefs(namespace).getBoolean(key, default)
    actual fun putBoolean(namespace: String, key: String, value: Boolean) { prefs(namespace).edit().putBoolean(key, value).apply() }
    actual fun clear(namespace: String) { prefs(namespace).edit().clear().apply() }
}

actual object Platform {
    actual val apiBaseUrl = "https://wanandroid.com"
    actual val managesCookies = false
    actual val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
    actual val versionName get() = AndroidPlatform.version
    actual fun nowMillis() = System.currentTimeMillis()
    actual fun formatDate(time: Long, pattern: String) = SimpleDateFormat(pattern, Locale.getDefault()).format(time)
    actual fun imageUrl(url: String) = url
    actual suspend fun cacheSize(): Long = withContext(Dispatchers.IO) {
        listOfNotNull(AndroidPlatform.context.cacheDir, AndroidPlatform.context.externalCacheDir)
            .sumOf { folder -> folder.walkTopDown().filter { it.isFile }.sumOf { it.length() } }
    }
    actual suspend fun clearCache() = withContext(Dispatchers.IO) {
        val loader = SingletonImageLoader.get(AndroidPlatform.context)
        loader.memoryCache?.clear()
        loader.diskCache?.clear()
        listOfNotNull(AndroidPlatform.context.cacheDir, AndroidPlatform.context.externalCacheDir)
            .forEach { folder -> folder.listFiles()?.forEach { it.deleteRecursively() } }
    }
}

actual fun createPlatformHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(OkHttp) { config() }

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) = BackHandler(enabled, onBack)

@Composable
actual fun ArticleWebView(url: String, textZoom: Int, modifier: Modifier) {
    AndroidView(
        modifier = modifier.clipToBounds(),
        factory = { context ->
            WebView(context).apply {
                webViewClient = WebViewClient()
                settings.javaScriptEnabled = true
                settings.javaScriptCanOpenWindowsAutomatically = false
                settings.loadsImagesAutomatically = true
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
                overScrollMode = WebView.OVER_SCROLL_NEVER
            }
        },
        update = { webView ->
            webView.settings.textZoom = textZoom
            if (webView.tag != url) {
                webView.tag = url
                webView.loadUrl(url)
            }
        },
        onRelease = { webView -> webView.stopLoading(); webView.destroy() },
    )
}
