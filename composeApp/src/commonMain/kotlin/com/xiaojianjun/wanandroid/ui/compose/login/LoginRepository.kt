package com.xiaojianjun.wanandroid.ui.compose.login

import com.xiaojianjun.wanandroid.model.api.WanApiClient

/**
 * Created by xiaojianjun on 2019-11-24.
 */
class LoginRepository {
    suspend fun login(username: String, password: String) =
        WanApiClient.apiService.login(username, password).apiData()
}
