package com.xiaojianjun.wanandroid.ui.main.navigation

import com.xiaojianjun.wanandroid.model.api.RetrofitClient

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class NavigationRepository {
    suspend fun getNavigations() = RetrofitClient.apiService.getNavigations().apiData()
}