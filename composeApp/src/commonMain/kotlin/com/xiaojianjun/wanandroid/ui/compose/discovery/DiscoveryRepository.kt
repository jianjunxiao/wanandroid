package com.xiaojianjun.wanandroid.ui.compose.discovery

import com.xiaojianjun.wanandroid.model.api.WanApiClient

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class DiscoveryRepository {
    suspend fun getBanners() = WanApiClient.apiService.getBanners().apiData()
    suspend fun getHotWords() = WanApiClient.apiService.getHotWords().apiData()
    suspend fun getFrequentlyWebsites() =
        WanApiClient.apiService.getFrequentlyWebsites().apiData()
}
