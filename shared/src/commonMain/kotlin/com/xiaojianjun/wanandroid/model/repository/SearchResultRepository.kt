package com.xiaojianjun.wanandroid.model.repository

import com.xiaojianjun.wanandroid.model.api.ApiService

/**
 * Created by xiaojianjun on 2019-11-28.
 */
class SearchResultRepository(
    private val apiService: ApiService,
) {

    suspend fun search(keywords: String, page: Int) =
        apiService.search(keywords, page).apiData()

}
