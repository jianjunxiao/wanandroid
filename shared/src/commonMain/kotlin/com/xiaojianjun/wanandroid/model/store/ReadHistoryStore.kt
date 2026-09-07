package com.xiaojianjun.wanandroid.model.store

import com.xiaojianjun.wanandroid.common.core.JsonCodec
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.platform.Platform
import com.xiaojianjun.wanandroid.platform.PlatformPreferences
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

interface ReadHistoryStorage {
    suspend fun queryAllReadHistory(): List<Article>
    suspend fun addReadHistory(article: Article)
    suspend fun deleteReadHistory(article: Article)
}

object ReadHistoryStore : ReadHistoryStorage {
    var storage: ReadHistoryStorage = PersistentReadHistoryStorage()
    override suspend fun queryAllReadHistory() = storage.queryAllReadHistory()
    override suspend fun addReadHistory(article: Article) = storage.addReadHistory(article)
    override suspend fun deleteReadHistory(article: Article) = storage.deleteReadHistory(article)
}

class PersistentReadHistoryStorage : ReadHistoryStorage {
    private val mutex = Mutex()
    private fun read(): List<Article> = JsonCodec.fromJson(
        PlatformPreferences.getString("sp_read_history", "articles", "[]")
    ) ?: emptyList()

    private fun write(articles: List<Article>) =
        PlatformPreferences.putString("sp_read_history", "articles", JsonCodec.toJson(articles))

    override suspend fun queryAllReadHistory(): List<Article> = withContext(Platform.ioDispatcher) {
        mutex.withLock { read().sortedByDescending { it.readTime } }
    }

    override suspend fun addReadHistory(article: Article) = withContext(Platform.ioDispatcher) {
        mutex.withLock {
            val item = article.copy(readTime = Platform.nowMillis())
            write(listOf(item) + read().filterNot { it.id == article.id })
        }
    }

    override suspend fun deleteReadHistory(article: Article) = withContext(Platform.ioDispatcher) {
        mutex.withLock { write(read().filterNot { it.id == article.id }) }
    }
}
