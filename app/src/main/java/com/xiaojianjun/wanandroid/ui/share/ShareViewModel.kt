package com.xiaojianjun.wanandroid.ui.share

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.base.BaseViewModel
import com.xiaojianjun.wanandroid.model.bean.UserInfo
import com.xiaojianjun.wanandroid.model.store.UserInfoStore

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
        launch(
            block = {
                submitting.value = true
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