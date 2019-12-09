package com.xiaojianjun.wanandroid.ui.collection

import com.xiaojianjun.wanandroid.model.api.RetrofitClient

/**
 * Created by xiaojianjun on 2019-11-28.
 */
class CollectionRepository {
    suspend fun getCollectionList(page: Int) =
        RetrofitClient.apiService.getCollectionList(page).apiData()
}