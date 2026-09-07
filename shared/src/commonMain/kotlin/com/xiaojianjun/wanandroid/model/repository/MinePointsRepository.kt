package com.xiaojianjun.wanandroid.model.repository

import com.xiaojianjun.wanandroid.model.api.ApiService

/**
 * Created by xiaojianjun on 2019-12-02.
 */
class MinePointsRepository(
    private val apiService: ApiService,
) {
    suspend fun getMyPoints() = apiService.getPoints().apiData()
    suspend fun getPointsRecord(page: Int) =
        apiService.getPointsRecord(page).apiData()
}
