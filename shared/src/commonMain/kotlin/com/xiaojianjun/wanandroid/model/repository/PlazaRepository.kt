package com.xiaojianjun.wanandroid.model.repository

import com.xiaojianjun.wanandroid.model.api.ApiService

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class PlazaRepository(
    private val apiService: ApiService,
) {
    suspend fun getUserArticleList(page: Int) =
        apiService.getUserArticleList(page).apiData()
}
