package com.xiaojianjun.wanandroid.model.room

import androidx.room.Room
import androidx.room.withTransaction
import com.xiaojianjun.wanandroid.App
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.store.ReadHistoryStorage

/**
 * Created by xiaojianjun on 2019-12-05.
 */
object RoomHelper : ReadHistoryStorage {

    private val appDatabase by lazy {
        Room.databaseBuilder(App.instance, AppDatabase::class.java, "database_wanandroid").build()
    }

    private val readHistoryDao by lazy { appDatabase.readHistoryDao() }

    override suspend fun queryAllReadHistory(): List<Article> {
        return readHistoryDao.queryAllReadHistory().map {
            it.article.toArticle().apply { tags = it.tags.map { tag -> tag.toTag() }.toMutableList() }
        }
    }

    override suspend fun addReadHistory(article: Article) {
        appDatabase.withTransaction {
            readHistoryDao.insertArticle(article.copy(readTime = System.currentTimeMillis()).toStoredArticle())
            readHistoryDao.queryAllTags(article.id).forEach { readHistoryDao.deleteTag(it) }
            article.tags.forEach { readHistoryDao.insertTag(it.copy(articleId = article.id).toStoredTag()) }
        }
    }

    override suspend fun deleteReadHistory(article: Article) {
        appDatabase.withTransaction {
            readHistoryDao.queryReadHistory(article.id)?.let { readHistory ->
                readHistoryDao.deleteArticle(readHistory.article)
                readHistory.tags.forEach { readHistoryDao.deleteTag(it) }
            }
        }
    }
}
