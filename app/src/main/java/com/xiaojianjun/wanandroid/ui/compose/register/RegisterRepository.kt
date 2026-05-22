package com.xiaojianjun.wanandroid.ui.compose.register

import com.xiaojianjun.wanandroid.model.api.RetrofitClient

/**
 * Created by xiaojianjun on 2019-11-24.
 */
class RegisterRepository {
    suspend fun register(username: String, password: String, repassword: String) =
        RetrofitClient.apiService.register(username, password, repassword).apiData()
}