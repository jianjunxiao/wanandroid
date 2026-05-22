package com.xiaojianjun.wanandroid.ui.compose.search

import com.xiaojianjun.wanandroid.model.api.RetrofitClient
import com.xiaojianjun.wanandroid.model.store.SearchHistoryStore

/**
 * Created by xiaojianjun on 2019-11-28.
 */
class SearchHistoryRepository {

    suspend fun getHotSearch() = RetrofitClient.apiService.getHotWords().apiData()

    fun saveSearchHistory(searchWords: String) {
        SearchHistoryStore.saveSearchHistory(searchWords)
    }

    fun deleteSearchHistory(searchWords: String) {
        SearchHistoryStore.deleteSearchHistory(searchWords)
    }

    fun getSearchHistory() = SearchHistoryStore.getSearchHistory()
}