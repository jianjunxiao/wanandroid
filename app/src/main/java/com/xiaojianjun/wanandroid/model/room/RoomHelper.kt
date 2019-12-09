package com.xiaojianjun.wanandroid.model.room

import androidx.room.Room
import com.xiaojianjun.wanandroid.App
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.bean.Tag

/**
 * Created by xiaojianjun on 2019-12-05.
 */
object RoomHelper {

    private val appDatabase by lazy {
        Room.databaseBuilder(App.instance, AppDatabase::class.java, "database_wanandroid").build()
    }

    private val readHistoryDao by lazy { appDatabase.readHistoryDao() }

    suspend fun queryAllReadHistory() = readHistoryDao.queryAll()
        .map { it.article.apply { tags = it.tags } }.reversed()

    suspend fun addReadHistory(article: Article) {
        readHistoryDao.queryArticle(article.id)?.let {
            readHistoryDao.deleteArticle(it)
        }
        readHistoryDao.insert(article.apply { primaryKeyId = 0 })
        article.tags.forEach {
            readHistoryDao.insertArticleTag(
                Tag(id = 0, articleId = article.id.toLong(), name = it.name, url = it.url)
            )
        }
    }

    suspend fun deleteReadHistory(article: Article) = readHistoryDao.deleteArticle(article)
}