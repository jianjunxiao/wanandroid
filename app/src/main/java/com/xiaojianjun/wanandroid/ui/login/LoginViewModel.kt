package com.xiaojianjun.wanandroid.ui.login

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.ui.base.BaseViewModel
import com.xiaojianjun.wanandroid.util.core.bus.Bus
import com.xiaojianjun.wanandroid.util.core.bus.USER_LOGIN_STATE_CHANGED

/**
 * Created by xiaojianjun on 2019-10-18.
 */
class LoginViewModel : BaseViewModel() {

    private val loginRepository by lazy { LoginRepository() }
    val submitting = MutableLiveData<Boolean>()
    val loginResult = MutableLiveData<Boolean>()


    fun login(account: String, password: String) {
        submitting.value = true
        launch(
            block = {
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