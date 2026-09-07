package com.xiaojianjun.wanandroid.model.repository

import com.xiaojianjun.wanandroid.model.api.ApiService

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class PopularRepository(
    private val apiService: ApiService,
) {
    suspend fun getTopArticleList() = apiService.getTopArticleList().apiData()
    suspend fun getArticleList(page: Int) = apiService.getArticleList(page).apiData()
}
