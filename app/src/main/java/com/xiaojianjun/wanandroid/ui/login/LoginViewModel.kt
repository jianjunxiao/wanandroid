package com.xiaojianjun.wanandroid.ui.login

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.base.BaseViewModel
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED

/**
 * Created by xiaojianjun on 2019-10-18.
 */
class LoginViewModel : BaseViewModel() {

    private val loginRepository by lazy { LoginRepository() }
    val submitting = MutableLiveData<Boolean>()
    val loginResult = MutableLiveData<Boolean>()


    fun login(account: String, password: String) {
        launch(
            block = {
                submitting.value = true
                val userInfo = loginRepository.login(account, password)
                UserInfoStore.setUserInfo(userInfo)
                Bus.post(USER_LOGIN_STATE_CHANGED, true)
                submitting.value = false
                loginResult.value = true
            },
            error = {
                submitting.value = false
                loginResult.value = false
            }
        )
    }

}