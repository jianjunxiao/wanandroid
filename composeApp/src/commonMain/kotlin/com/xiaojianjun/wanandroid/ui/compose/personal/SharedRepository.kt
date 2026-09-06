package com.xiaojianjun.wanandroid.ui.compose.personal

import com.xiaojianjun.wanandroid.model.api.WanApiClient

/**
 * Created by xiaojianjun on 2019-12-03.
 */
class SharedRepository {
    suspend fun getSharedArticleList(page: Int) =
        WanApiClient.apiService.getSharedArticleList(page).apiData()

    suspend fun deleteShared(id: Long) = WanApiClient.apiService.deleteShare(id).apiData()
}
