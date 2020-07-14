package com.xiaojianjun.wanandroid.ui.register

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.ui.base.BaseViewModel
import com.xiaojianjun.wanandroid.util.core.bus.Bus
import com.xiaojianjun.wanandroid.util.core.bus.USER_LOGIN_STATE_CHANGED

/**
 * Created by xiaojianjun on 2019-10-18.
 */
class RegisterViewModel : BaseViewModel() {
    private val registerRepository by lazy { RegisterRepository() }
    val submitting = MutableLiveData<Boolean>()
    val registerResult = MutableLiveData<Boolean>()

    fun register(account: String, password: String, confirmPassword: String) {
        submitting.value = true
        launch(
            block = {
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