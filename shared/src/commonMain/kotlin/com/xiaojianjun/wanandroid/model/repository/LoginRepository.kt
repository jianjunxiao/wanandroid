package com.xiaojianjun.wanandroid.model.repository

import com.xiaojianjun.wanandroid.model.api.ApiService

/**
 * Created by xiaojianjun on 2019-11-24.
 */
class LoginRepository(
    private val apiService: ApiService,
) {
    suspend fun login(username: String, password: String) =
        apiService.login(username, password).apiData()
}
