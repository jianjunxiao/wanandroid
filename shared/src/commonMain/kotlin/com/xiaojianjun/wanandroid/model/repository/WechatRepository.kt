package com.xiaojianjun.wanandroid.model.repository

import com.xiaojianjun.wanandroid.model.api.ApiService

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class WechatRepository(
    private val apiService: ApiService,
) {
    suspend fun getWechatCategories() = apiService.getWechatCategories().apiData()
    suspend fun getWechatArticleList(page: Int, id: Int) =
        apiService.getWechatArticleList(page, id).apiData()
}
