package com.xiaojianjun.wanandroid.ui.compose.system

import com.xiaojianjun.wanandroid.model.api.WanApiClient

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class SystemRepository {
    suspend fun getArticleCategories() = WanApiClient.apiService.getArticleCategories().apiData()
}
