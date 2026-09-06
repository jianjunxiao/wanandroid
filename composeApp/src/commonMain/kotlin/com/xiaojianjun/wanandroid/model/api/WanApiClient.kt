package com.xiaojianjun.wanandroid.model.api

import com.xiaojianjun.wanandroid.platform.Platform
import com.xiaojianjun.wanandroid.platform.createPlatformHttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cookies.HttpCookies
import io.ktor.client.request.post

object WanApiClient {
    val cookies by lazy { PersistentCookiesStorage() }
    val client by lazy {
        createPlatformHttpClient {
            expectSuccess = true
            install(HttpTimeout) {
                requestTimeoutMillis = 10_000
                connectTimeoutMillis = 10_000
            }
            if (!Platform.managesCookies) {
                install(HttpCookies) { storage = cookies }
            }
        }
    }
    val apiService by lazy { ApiService(client) }
    fun importLegacyCookies(legacy: List<StoredCookie>) = cookies.importLegacy(legacy)
    suspend fun clearCookie() {
        cookies.clear()
        if (Platform.managesCookies) client.post(Platform.apiBaseUrl + "/session/clear")
    }
    fun hasCookie(): Boolean = Platform.managesCookies || cookies.hasCookies()
}
