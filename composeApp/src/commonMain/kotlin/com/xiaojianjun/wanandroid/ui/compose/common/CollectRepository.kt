package com.xiaojianjun.wanandroid.ui.compose.common

import com.xiaojianjun.wanandroid.model.api.WanApiClient

/**
 * Created by xiaojianjun on 2019-11-27.
 */
class CollectRepository {
    suspend fun collect(id: Long) = WanApiClient.apiService.collect(id)
    suspend fun uncollect(id: Long) = WanApiClient.apiService.uncollect(id)
}
