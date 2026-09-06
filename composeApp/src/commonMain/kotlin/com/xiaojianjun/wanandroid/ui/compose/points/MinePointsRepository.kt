package com.xiaojianjun.wanandroid.ui.compose.points

import com.xiaojianjun.wanandroid.model.api.WanApiClient

/**
 * Created by xiaojianjun on 2019-12-02.
 */
class MinePointsRepository {
    suspend fun getMyPoints() = WanApiClient.apiService.getPoints().apiData()
    suspend fun getPointsRecord(page: Int) =
        WanApiClient.apiService.getPointsRecord(page).apiData()
}
