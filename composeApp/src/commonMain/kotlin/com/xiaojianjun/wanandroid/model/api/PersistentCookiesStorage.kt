package com.xiaojianjun.wanandroid.model.api

import com.xiaojianjun.wanandroid.common.core.JsonCodec
import com.xiaojianjun.wanandroid.platform.Platform
import com.xiaojianjun.wanandroid.platform.PlatformPreferences
import io.ktor.client.plugins.cookies.CookiesStorage
import io.ktor.http.Cookie
import io.ktor.http.URLProtocol
import io.ktor.http.Url
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.Serializable

@Serializable
data class StoredCookie(
    val name: String,
    val value: String,
    val domain: String,
    val path: String = "/",
    val expiresAt: Long? = null,
    val secure: Boolean = false,
    val httpOnly: Boolean = false,
    val hostOnly: Boolean = true,
)

class PersistentCookiesStorage : CookiesStorage {
    private val mutex = Mutex()
    private fun read(): List<StoredCookie> = JsonCodec.fromJson(
        PlatformPreferences.getString("sp_session", "cookies", "[]")
    ) ?: emptyList()

    private fun write(cookies: List<StoredCookie>) =
        PlatformPreferences.putString("sp_session", "cookies", JsonCodec.toJson(cookies))

    fun hasCookies(): Boolean = read().any { it.expiresAt == null || it.expiresAt > Platform.nowMillis() }
    suspend fun clear() = mutex.withLock { PlatformPreferences.clear("sp_session") }

    // Android 首次迁移导入旧版 Cookie，后续只使用共享持久化实现。
    fun importLegacy(cookies: List<StoredCookie>) {
        if (read().isEmpty()) write(cookies)
    }

    override suspend fun get(requestUrl: Url): List<Cookie> = mutex.withLock {
        val now = Platform.nowMillis()
        read().filter { cookie ->
            (cookie.expiresAt == null || cookie.expiresAt > now) &&
                (requestUrl.host == cookie.domain || (!cookie.hostOnly && requestUrl.host.endsWith("." + cookie.domain))) &&
                (requestUrl.encodedPath == cookie.path || requestUrl.encodedPath.startsWith(cookie.path.trimEnd('/') + "/")) &&
                (!cookie.secure || requestUrl.protocol == URLProtocol.HTTPS)
        }.map { Cookie(it.name, it.value, domain = it.domain, path = it.path, secure = it.secure, httpOnly = it.httpOnly) }
    }

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) = mutex.withLock {
        val domain = cookie.domain?.trimStart('.') ?: requestUrl.host
        if (requestUrl.host != domain && !requestUrl.host.endsWith(".$domain")) return@withLock
        val path = cookie.path ?: requestUrl.encodedPath.substringBeforeLast('/', "").ifEmpty { "/" }
        val cookies = read().filterNot { it.name == cookie.name && it.domain == domain && it.path == path }.toMutableList()
        val expiresAt = cookie.maxAge?.let { Platform.nowMillis() + it * 1000L } ?: cookie.expires?.timestamp
        if (cookie.value.isNotEmpty() && (expiresAt == null || expiresAt > Platform.nowMillis())) {
            cookies += StoredCookie(cookie.name, cookie.value, domain, path, expiresAt, cookie.secure, cookie.httpOnly, cookie.domain == null)
        }
        write(cookies)
    }

    override fun close() = Unit
}
