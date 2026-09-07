package com.xiaojianjun.wanandroid.model.store

import com.xiaojianjun.wanandroid.model.api.WanApiClient

/**
 * Created by xiaojianjun on 2020/4/7.
 * 是否登录，有cookie/token，且用户信息存在
 */
fun isLogin() = UserInfoStore.getUserInfo() != null && WanApiClient.hasCookie()
