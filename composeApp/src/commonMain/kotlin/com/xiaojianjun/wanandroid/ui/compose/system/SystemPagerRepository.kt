package com.xiaojianjun.wanandroid.ui.compose.system

import com.xiaojianjun.wanandroid.model.api.WanApiClient

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class SystemPagerRepository {

    suspend fun getArticleListByCid(page: Int, cid: Int) =
        WanApiClient.apiService.getArticleListByCid(page, cid).apiData()
}
