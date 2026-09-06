package com.xiaojianjun.wanandroid.ui.compose.home

import com.xiaojianjun.wanandroid.model.api.WanApiClient

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class PlazaRepository {
    suspend fun getUserArticleList(page: Int) =
        WanApiClient.apiService.getUserArticleList(page).apiData()
}
