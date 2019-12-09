package com.xiaojianjun.wanandroid.ui.settings

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.ui.base.BaseViewModel
import com.xiaojianjun.wanandroid.util.core.bus.Bus
import com.xiaojianjun.wanandroid.util.core.bus.USER_LOGIN_STATE_CHANGED

/**
 * Created by xiaojianjun on 2019-11-30.
 */
class SettingsViewModel : BaseViewModel() {

    val isLogin = MutableLiveData<Boolean>()

    fun getLoginStatus() {
        isLogin.value = userRepository.isLogin()
    }

    fun logout() {
        userRepository.clearLoginState()
        Bus.post(USER_LOGIN_STATE_CHANGED, false)
    }
}