package com.xiaojianjun.wanandroid.ui.compose.navigationpage

import com.xiaojianjun.wanandroid.model.api.WanApiClient

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class NavigationRepository {
    suspend fun getNavigations() = WanApiClient.apiService.getNavigations().apiData()
}
