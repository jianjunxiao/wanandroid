package com.xiaojianjun.wanandroid.ui.compose.personal

import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.store.ReadHistoryStore

/**
 * Created by xiaojianjun on 2019-11-28.
 */
class HistoryRepository {

    suspend fun getReadHistory() = ReadHistoryStore.queryAllReadHistory()
    suspend fun deleteHistory(article: Article) = ReadHistoryStore.deleteReadHistory(article)

}
