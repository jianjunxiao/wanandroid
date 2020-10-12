package com.xiaojianjun.wanandroid.model.room

import androidx.room.Room
import com.xiaojianjun.wanandroid.App
import com.xiaojianjun.wanandroid.model.bean.Article

/**
 * Created by xiaojianjun on 2019-12-05.
 */
object RoomHelper {

    private val appDatabase by lazy {
        Room.databaseBuilder(App.instance, AppDatabase::class.java, "database_wanandroid").build()
    }

    private val readHistoryDao by lazy { appDatabase.readHistoryDao() }

    suspend fun queryAllReadHistory(): List<Article> {
        return readHistoryDao.queryAllReadHistory().map {
            it.article.apply { tags = it.tags }
        }
    }

    suspend fun addReadHistory(article: Article) {
        article.readTime = System.currentTimeMillis()
        readHistoryDao.insertArticle(article)
        article.tags.forEach {
            readHistoryDao.insertTag(it.apply { it.articleId = article.id })
        }
    }

    suspend fun deleteReadHistory(article: Article) {
        readHistoryDao.queryReadHistory(article.id)?.let { readHistory ->
            readHistoryDao.deleteArticle(readHistory.article)
            readHistory.tags.forEach { readHistoryDao.deleteTag(it) }
        }
    }
}