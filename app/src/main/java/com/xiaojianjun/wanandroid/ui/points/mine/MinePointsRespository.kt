package com.xiaojianjun.wanandroid.ui.points.mine

import com.xiaojianjun.wanandroid.model.api.RetrofitClient

/**
 * Created by xiaojianjun on 2019-12-02.
 */
class MinePointsRespository {
    suspend fun getMyPoints() = RetrofitClient.apiService.getPoints().apiData()
    suspend fun getPointsRecord(page: Int) =
        RetrofitClient.apiService.getPointsRecord(page).apiData()
}