package com.xiaojianjun.wanandroid.ui.register

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.base.BaseViewModel
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.xiaojianjun.wanandroid.model.store.UserInfoStore

/**
 * Created by xiaojianjun on 2019-10-18.
 */
class RegisterViewModel : BaseViewModel() {
    private val registerRepository by lazy { RegisterRepository() }
    val submitting = MutableLiveData<Boolean>()
    val registerResult = MutableLiveData<Boolean>()

    fun register(account: String, password: String, confirmPassword: String) {
        launch(
            block = {
                submitting.value = true
                val userInfo = registerRepository.register(account, password, confirmPassword)
                UserInfoStore.setUserInfo(userInfo)
                Bus.post(USER_LOGIN_STATE_CHANGED, true)
                registerResult.value = true
                submitting.value = false
            },
            error = {
                registerResult.value = false
                submitting.value = false
            }
        )
    }
}