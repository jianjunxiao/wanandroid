package com.xiaojianjun.wanandroid.ui.compose.home

import com.xiaojianjun.wanandroid.model.api.RetrofitClient

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class LatestRepository {
    suspend fun getProjectList(page: Int) = RetrofitClient.apiService.getProjectList(page).apiData()
}