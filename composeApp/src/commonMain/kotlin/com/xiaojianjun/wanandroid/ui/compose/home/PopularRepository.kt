package com.xiaojianjun.wanandroid.ui.compose.home

import com.xiaojianjun.wanandroid.model.api.WanApiClient

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class PopularRepository {
    suspend fun getTopArticleList() = WanApiClient.apiService.getTopArticleList().apiData()
    suspend fun getArticleList(page: Int) = WanApiClient.apiService.getArticleList(page).apiData()
}
