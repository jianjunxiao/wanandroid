package com.xiaojianjun.wanandroid.model.repository

import com.xiaojianjun.wanandroid.model.api.ApiService
import com.xiaojianjun.wanandroid.model.store.SearchHistoryStorage

/**
 * Created by xiaojianjun on 2019-11-28.
 */
class SearchHistoryRepository(
    private val apiService: ApiService,
    private val history: SearchHistoryStorage,
) {

    suspend fun getHotSearch() = apiService.getHotWords().apiData()

    fun saveSearchHistory(searchWords: String) {
        history.saveSearchHistory(searchWords)
    }

    fun deleteSearchHistory(searchWords: String) {
        history.deleteSearchHistory(searchWords)
    }

    fun getSearchHistory() = history.getSearchHistory()
}
