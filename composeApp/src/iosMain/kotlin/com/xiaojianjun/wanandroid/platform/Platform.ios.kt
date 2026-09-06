@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class, androidx.compose.ui.ExperimentalComposeUiApi::class)

package com.xiaojianjun.wanandroid.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.darwin.Darwin
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import platform.Foundation.*
import platform.UIKit.*
import platform.WebKit.*

actual object PlatformPreferences {
    private val defaults get() = NSUserDefaults.standardUserDefaults
    private fun fullKey(namespace: String, key: String) = "$namespace.$key"
    actual fun getString(namespace: String, key: String, default: String) = defaults.stringForKey(fullKey(namespace, key)) ?: default
    actual fun putString(namespace: String, key: String, value: String) { defaults.setObject(value, fullKey(namespace, key)) }
    actual fun getInt(namespace: String, key: String, default: Int) =
        if (defaults.objectForKey(fullKey(namespace, key)) == null) default else defaults.integerForKey(fullKey(namespace, key)).toInt()
    actual fun putInt(namespace: String, key: String, value: Int) { defaults.setInteger(value.toLong(), fullKey(namespace, key)) }
    actual fun getBoolean(namespace: String, key: String, default: Boolean) =
        if (defaults.objectForKey(fullKey(namespace, key)) == null) default else defaults.boolForKey(fullKey(namespace, key))
    actual fun putBoolean(namespace: String, key: String, value: Boolean) { defaults.setBool(value, fullKey(namespace, key)) }
    actual fun clear(namespace: String) {
        defaults.dictionaryRepresentation().keys.filterIsInstance<String>()
            .filter { it.startsWith("$namespace.") }.forEach { defaults.removeObjectForKey(it) }
    }
}

actual object Platform {
    actual val apiBaseUrl = "https://wanandroid.com"
    actual val managesCookies = false
    actual val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
    actual val versionName get() = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleShortVersionString") as? String ?: "1.0.6"
    actual fun nowMillis() = (NSDate().timeIntervalSince1970 * 1000).toLong()
    actual fun formatDate(time: Long, pattern: String): String = NSDateFormatter().apply {
        dateFormat = pattern
        locale = NSLocale.currentLocale
    }.stringFromDate(NSDate.dateWithTimeIntervalSince1970(time / 1000.0))
    actual fun imageUrl(url: String) = url
    private fun cacheDirectory() = NSSearchPathForDirectoriesInDomains(NSCachesDirectory, NSUserDomainMask, true).first() as String
    actual suspend fun cacheSize(): Long = withContext(Dispatchers.Default) {
        val manager = NSFileManager.defaultManager
        val folder = cacheDirectory()
        manager.subpathsAtPath(folder).orEmpty().filterIsInstance<String>().sumOf {
            val attributes = manager.attributesOfItemAtPath("$folder/$it", null)
            if (attributes?.get(NSFileType) == NSFileTypeRegular) {
                (attributes?.get(NSFileSize) as? NSNumber)?.longLongValue ?: 0L
            } else 0L
        }
    }
    actual suspend fun clearCache() = withContext(Dispatchers.Default) {
        com.xiaojianjun.wanandroid.common.core.ImageCache.clear()
        NSURLCache.sharedURLCache.removeAllCachedResponses()
        val folder = cacheDirectory()
        NSFileManager.defaultManager.contentsOfDirectoryAtPath(folder, null).orEmpty().filterIsInstance<String>()
            .forEach { NSFileManager.defaultManager.removeItemAtPath("$folder/$it", null) }
    }
}

actual fun createPlatformHttpClient(config: HttpClientConfig<*>.() -> Unit) = HttpClient(Darwin) { config() }

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    androidx.navigationevent.compose.NavigationEventHandler(
        state = androidx.navigationevent.compose.rememberNavigationEventState(androidx.navigationevent.NavigationEventInfo.None),
        isBackEnabled = enabled,
        isForwardEnabled = false,
        onBackCompleted = onBack,
    )
}

@Composable
actual fun ArticleWebView(url: String, textZoom: Int, modifier: Modifier) {
    key(url) {
        UIKitView(
            modifier = modifier,
            factory = {
                WKWebView().apply {
                    allowsBackForwardNavigationGestures = true
                    NSURL.URLWithString(url)?.let { loadRequest(NSURLRequest.requestWithURL(it)) }
                }
            },
            update = { webView -> webView.pageZoom = textZoom / 100.0 },
            onRelease = { webView ->
                webView.stopLoading()
                webView.navigationDelegate = null
            },
        )
    }
}
