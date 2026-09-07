package com.xiaojianjun.wanandroid.model.repository

import com.xiaojianjun.wanandroid.model.api.ApiService

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class ProjectRepository(
    private val apiService: ApiService,
) {
    suspend fun getProjectCategories() = apiService.getProjectCategories().apiData()
    suspend fun getProjectListByCid(page: Int, cid: Int) =
        apiService.getProjectListByCid(page, cid).apiData()
}
