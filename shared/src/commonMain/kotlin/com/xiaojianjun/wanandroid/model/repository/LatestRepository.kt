package com.xiaojianjun.wanandroid.model.repository

import com.xiaojianjun.wanandroid.model.api.ApiService

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class LatestRepository(
    private val apiService: ApiService,
) {
    suspend fun getProjectList(page: Int) = apiService.getProjectList(page).apiData()
}
