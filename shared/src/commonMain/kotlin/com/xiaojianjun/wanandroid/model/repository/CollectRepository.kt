package com.xiaojianjun.wanandroid.model.repository

import com.xiaojianjun.wanandroid.model.api.ApiService

/**
 * Created by xiaojianjun on 2019-11-27.
 */
class CollectRepository(
    private val apiService: ApiService,
) {
    suspend fun collect(id: Long) = apiService.collect(id)
    suspend fun uncollect(id: Long) = apiService.uncollect(id)
}
