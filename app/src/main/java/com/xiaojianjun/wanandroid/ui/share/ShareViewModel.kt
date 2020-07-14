package com.xiaojianjun.wanandroid.ui.share

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.model.bean.UserInfo
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.ui.base.BaseViewModel

/**
 * Created by xiaojianjun on 2019-11-30.
 */
class ShareViewModel : BaseViewModel() {

    private val shareRepository by lazy { ShareRepository() }
    val userInfo = MutableLiveData<UserInfo>()
    val submitting = MutableLiveData<Boolean>()
    val shareResult = MutableLiveData<Boolean>()

    fun getUserInfo() {
        userInfo.value = UserInfoStore.getUserInfo()
    }

    fun shareArticle(title: String, link: String) {
        submitting.value = true
        launch(
            block = {
                shareRepository.shareArticle(title, link)
                shareResult.value = true
                submitting.value = false
            },
            error = {
                shareResult.value = false
                submitting.value = false
            }
        )
    }

}