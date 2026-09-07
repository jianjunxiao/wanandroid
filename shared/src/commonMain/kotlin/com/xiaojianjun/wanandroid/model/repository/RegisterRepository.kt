package com.xiaojianjun.wanandroid.model.repository

import com.xiaojianjun.wanandroid.model.api.ApiService

/**
 * Created by xiaojianjun on 2019-11-24.
 */
class RegisterRepository(
    private val apiService: ApiService,
) {
    suspend fun register(username: String, password: String, repassword: String) =
        apiService.register(username, password, repassword).apiData()
}
