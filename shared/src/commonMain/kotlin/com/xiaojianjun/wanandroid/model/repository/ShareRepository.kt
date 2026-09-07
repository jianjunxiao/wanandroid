package com.xiaojianjun.wanandroid.model.repository

import com.xiaojianjun.wanandroid.model.api.ApiService

/**
 * Created by xiaojianjun on 2019-12-01.
 */
class ShareRepository(
    private val apiService: ApiService,
) {
    suspend fun shareArticle(title: String, link: String) =
        apiService.shareArticle(title, link).apiData()
}
