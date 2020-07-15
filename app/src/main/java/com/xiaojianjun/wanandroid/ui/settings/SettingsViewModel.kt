package com.xiaojianjun.wanandroid.ui.settings

import com.xiaojianjun.wanandroid.model.api.RetrofitClient
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.base.BaseViewModel
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED

/**
 * Created by xiaojianjun on 2019-11-30.
 */
class SettingsViewModel : BaseViewModel() {
    fun logout() {
        UserInfoStore.clearUserInfo()
        RetrofitClient.clearCookie()
        Bus.post(USER_LOGIN_STATE_CHANGED, false)
    }
}