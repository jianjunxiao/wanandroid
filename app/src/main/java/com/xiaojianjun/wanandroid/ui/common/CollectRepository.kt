package com.xiaojianjun.wanandroid.ui.common

import com.xiaojianjun.wanandroid.model.api.RetrofitClient

/**
 * Created by xiaojianjun on 2019-11-27.
 */
class CollectRepository {
    suspend fun collect(id: Long) = RetrofitClient.apiService.collect(id)
    suspend fun uncollect(id: Long) = RetrofitClient.apiService.uncollect(id)
}