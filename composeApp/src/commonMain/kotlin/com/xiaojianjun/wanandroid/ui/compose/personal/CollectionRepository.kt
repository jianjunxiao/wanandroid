package com.xiaojianjun.wanandroid.ui.compose.personal

import com.xiaojianjun.wanandroid.model.api.WanApiClient

/**
 * Created by xiaojianjun on 2019-11-28.
 */
class CollectionRepository {
    suspend fun getCollectionList(page: Int) =
        WanApiClient.apiService.getCollectionList(page).apiData()
}
