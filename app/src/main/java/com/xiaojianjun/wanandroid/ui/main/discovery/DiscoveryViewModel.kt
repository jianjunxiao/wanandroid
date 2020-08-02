package com.xiaojianjun.wanandroid.ui.main.discovery

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.base.BaseViewModel
import com.xiaojianjun.wanandroid.model.bean.Banner
import com.xiaojianjun.wanandroid.model.bean.Frequently
import com.xiaojianjun.wanandroid.model.bean.HotWord

class DiscoveryViewModel : BaseViewModel() {

    private val dicoveryRepository by lazy { DiscoveryRepository() }
    val banners = MutableLiveData<List<Banner>>()
    val hotWords = MutableLiveData<List<HotWord>>()
    val frequentlyList = MutableLiveData<List<Frequently>>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    fun getData() {
        launch(
            block = {
                refreshStatus.value = true
                reloadStatus.value = false

                banners.value = dicoveryRepository.getBanners()
                hotWords.value = dicoveryRepository.getHotWords()
                frequentlyList.value = dicoveryRepository.getFrequentlyWebsites()

                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = banners.value.isNullOrEmpty()
                        && hotWords.value.isNullOrEmpty()
                        && frequentlyList.value.isNullOrEmpty()
            }
        )
    }

}
