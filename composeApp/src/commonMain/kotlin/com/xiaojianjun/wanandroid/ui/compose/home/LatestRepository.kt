package com.xiaojianjun.wanandroid.ui.compose.home

import com.xiaojianjun.wanandroid.model.api.WanApiClient

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class LatestRepository {
    suspend fun getProjectList(page: Int) = WanApiClient.apiService.getProjectList(page).apiData()
}
