package com.xiaojianjun.wanandroid.ui.main.navigation

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.model.bean.Navigation
import com.xiaojianjun.wanandroid.ui.base.BaseViewModel

class NavigationViewModel : BaseViewModel() {

    private val navigationRepository by lazy { NavigationRepository() }
    val navigations = MutableLiveData<List<Navigation>>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    fun getNavigations() {
        refreshStatus.value = true
        reloadStatus.value = false
        launch(
            block = {
                navigations.value = navigationRepository.getNavigations()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = navigations.value.isNullOrEmpty()
            }
        )
    }
}
