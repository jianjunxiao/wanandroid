package com.xiaojianjun.wanandroid.model.repository

import com.xiaojianjun.wanandroid.model.api.ApiService

/**
 * Created by xiaojianjun on 2019-12-02.
 */
class PointsRankRepository(
    private val apiService: ApiService,
) {
    suspend fun getPointsRank(page: Int) =
        apiService.getPointsRank(page).apiData()
}
