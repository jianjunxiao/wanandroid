package com.xiaojianjun.wanandroid.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.PlatformTextStyle
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import kotlinx.coroutines.CoroutineDispatcher

expect object PlatformPreferences {
    fun getString(namespace: String, key: String, default: String = ""): String
    fun putString(namespace: String, key: String, value: String)
    fun getInt(namespace: String, key: String, default: Int): Int
    fun putInt(namespace: String, key: String, value: Int)
    fun getBoolean(namespace: String, key: String, default: Boolean): Boolean
    fun putBoolean(namespace: String, key: String, value: Boolean)
    fun clear(namespace: String)
}

expect object Platform {
    val apiBaseUrl: String
    val managesCookies: Boolean
    val ioDispatcher: CoroutineDispatcher
    val versionName: String
    fun nowMillis(): Long
    fun formatDate(time: Long, pattern: String): String
    fun imageUrl(url: String): String
    suspend fun cacheSize(): Long
    suspend fun clearCache()
}

expect fun createPlatformHttpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient
expect fun articleTagPlatformStyle(): PlatformTextStyle?

@Composable
expect fun PlatformBackHandler(enabled: Boolean = true, onBack: () -> Unit)

@Composable
expect fun ArticleWebView(url: String, textZoom: Int, modifier: Modifier = Modifier)

@androidx.compose.runtime.Composable
expect fun ArticleToolbarAction(url: String, modifier: androidx.compose.ui.Modifier)
