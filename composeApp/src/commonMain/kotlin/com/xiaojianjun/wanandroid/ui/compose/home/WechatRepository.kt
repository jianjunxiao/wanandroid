package com.xiaojianjun.wanandroid.ui.compose.home

import com.xiaojianjun.wanandroid.model.api.WanApiClient

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class WechatRepository {
    suspend fun getWechatCategories() = WanApiClient.apiService.getWechatCategories().apiData()
    suspend fun getWechatArticleList(page: Int, id: Int) =
        WanApiClient.apiService.getWechatArticleList(page, id).apiData()
}
