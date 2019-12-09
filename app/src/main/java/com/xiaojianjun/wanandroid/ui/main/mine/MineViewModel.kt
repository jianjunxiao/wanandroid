package com.xiaojianjun.wanandroid.ui.main.mine

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.model.bean.UserInfo
import com.xiaojianjun.wanandroid.ui.base.BaseViewModel

class MineViewModel : BaseViewModel() {

    private val mineRespository by lazy { MineRespository() }

    val userInfo = MutableLiveData<UserInfo?>()
    val isLogin = MutableLiveData<Boolean>()

    fun getUserInfo() {
        isLogin.value = userRepository.isLogin()
        userInfo.value = userRepository.getUserInfo()
    }
}
