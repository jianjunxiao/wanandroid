package com.xiaojianjun.wanandroid.model.repository

import com.xiaojianjun.wanandroid.model.api.ApiService

/**
 * Created by xiaojianjun on 2019-09-18.
 */
class SystemPagerRepository(
    private val apiService: ApiService,
) {

    suspend fun getArticleListByCid(page: Int, cid: Int) =
        apiService.getArticleListByCid(page, cid).apiData()
}
