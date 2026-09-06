package com.xiaojianjun.wanandroid.ui.compose.home

import com.xiaojianjun.wanandroid.model.api.WanApiClient

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class ProjectRepository {
    suspend fun getProjectCategories() = WanApiClient.apiService.getProjectCategories().apiData()
    suspend fun getProjectListByCid(page: Int, cid: Int) =
        WanApiClient.apiService.getProjectListByCid(page, cid).apiData()
}
