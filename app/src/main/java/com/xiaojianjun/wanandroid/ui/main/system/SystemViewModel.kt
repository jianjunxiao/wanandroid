package com.xiaojianjun.wanandroid.ui.main.system

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.model.bean.Category
import com.xiaojianjun.wanandroid.base.BaseViewModel

class SystemViewModel : BaseViewModel() {
    private val systemRepository by lazy { SystemRepository() }
    val categories: MutableLiveData<MutableList<Category>> = MutableLiveData()
    val loadingStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()


    fun getArticleCategory() {
        launch(
            block = {
                loadingStatus.value = true
                reloadStatus.value = false
                categories.value = systemRepository.getArticleCategories()
                loadingStatus.value = false
            },
            error = {
                loadingStatus.value = false
                reloadStatus.value = true
            }
        )
    }

}
