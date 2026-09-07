package com.xiaojianjun.wanandroid.model.repository

import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.store.ReadHistoryStorage

/**
 * Created by xiaojianjun on 2019-11-28.
 */
class HistoryRepository(
    private val storage: ReadHistoryStorage,
) {

    suspend fun addHistory(article: Article) = storage.addReadHistory(article)
    suspend fun getReadHistory() = storage.queryAllReadHistory()
    suspend fun deleteHistory(article: Article) = storage.deleteReadHistory(article)

}
