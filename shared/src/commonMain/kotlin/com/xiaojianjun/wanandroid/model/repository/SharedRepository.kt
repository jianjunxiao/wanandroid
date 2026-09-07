package com.xiaojianjun.wanandroid.model.repository

import com.xiaojianjun.wanandroid.model.api.ApiService

/**
 * Created by xiaojianjun on 2019-12-03.
 */
class SharedRepository(
    private val apiService: ApiService,
) {
    suspend fun getSharedArticleList(page: Int) =
        apiService.getSharedArticleList(page).apiData()

    suspend fun deleteShared(id: Long) = apiService.deleteShare(id).apiData()
}
