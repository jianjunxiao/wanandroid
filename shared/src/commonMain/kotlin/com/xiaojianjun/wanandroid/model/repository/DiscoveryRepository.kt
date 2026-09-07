package com.xiaojianjun.wanandroid.model.repository

import com.xiaojianjun.wanandroid.model.api.ApiService

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class DiscoveryRepository(
    private val apiService: ApiService,
) {
    suspend fun getBanners() = apiService.getBanners().apiData()
    suspend fun getHotWords() = apiService.getHotWords().apiData()
    suspend fun getFrequentlyWebsites() =
        apiService.getFrequentlyWebsites().apiData()
}
