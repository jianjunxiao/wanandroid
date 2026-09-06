package com.xiaojianjun.wanandroid.ui.compose.search

import com.xiaojianjun.wanandroid.model.api.WanApiClient

/**
 * Created by xiaojianjun on 2019-11-28.
 */
class SearchResultRepository {

    suspend fun search(keywords: String, page: Int) =
        WanApiClient.apiService.search(keywords, page).apiData()

}
