package com.xiaojianjun.wanandroid.ui.compose.home

import com.xiaojianjun.wanandroid.model.api.RetrofitClient

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class PlazaRepository {
    suspend fun getUserArticleList(page: Int) =
        RetrofitClient.apiService.getUserArticleList(page).apiData()
}