package com.xiaojianjun.wanandroid.model.repository

import com.xiaojianjun.wanandroid.model.api.ApiService

/**
 * Created by xiaojianjun on 2019-11-28.
 */
class CollectionRepository(
    private val apiService: ApiService,
) {
    suspend fun getCollectionList(page: Int) =
        apiService.getCollectionList(page).apiData()
}
