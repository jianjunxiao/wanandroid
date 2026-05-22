package com.xiaojianjun.wanandroid.ui.compose.points

import com.xiaojianjun.wanandroid.model.api.RetrofitClient

/**
 * Created by xiaojianjun on 2019-12-02.
 */
class MinePointsRepository {
    suspend fun getMyPoints() = RetrofitClient.apiService.getPoints().apiData()
    suspend fun getPointsRecord(page: Int) =
        RetrofitClient.apiService.getPointsRecord(page).apiData()
}