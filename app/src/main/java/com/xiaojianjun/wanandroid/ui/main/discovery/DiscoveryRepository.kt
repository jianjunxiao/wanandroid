package com.xiaojianjun.wanandroid.ui.main.discovery

import com.xiaojianjun.wanandroid.model.api.RetrofitClient

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class DiscoveryRepository {
    suspend fun getBanners() = RetrofitClient.apiService.getBanners().apiData()
    suspend fun getHotWords() = RetrofitClient.apiService.getHotWords().apiData()
    suspend fun getFrequentlyWebsites() =
        RetrofitClient.apiService.getFrequentlyWebsites().apiData()
}