package com.xiaojianjun.wanandroid.ui.compose.points

import com.xiaojianjun.wanandroid.model.api.RetrofitClient

/**
 * Created by xiaojianjun on 2019-12-02.
 */
class PointsRankRepository {
    suspend fun getPointsRank(page: Int) =
        RetrofitClient.apiService.getPointsRank(page).apiData()
}