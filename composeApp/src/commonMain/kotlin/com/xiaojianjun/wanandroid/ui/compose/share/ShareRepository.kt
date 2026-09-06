package com.xiaojianjun.wanandroid.ui.compose.share

import com.xiaojianjun.wanandroid.model.api.WanApiClient

/**
 * Created by xiaojianjun on 2019-12-01.
 */
class ShareRepository {
    suspend fun shareArticle(title: String, link: String) =
        WanApiClient.apiService.shareArticle(title, link).apiData()
}
