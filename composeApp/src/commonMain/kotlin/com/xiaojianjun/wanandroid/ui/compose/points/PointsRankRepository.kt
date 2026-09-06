package com.xiaojianjun.wanandroid.ui.compose.points

import com.xiaojianjun.wanandroid.model.api.WanApiClient

/**
 * Created by xiaojianjun on 2019-12-02.
 */
class PointsRankRepository {
    suspend fun getPointsRank(page: Int) =
        WanApiClient.apiService.getPointsRank(page).apiData()
}
