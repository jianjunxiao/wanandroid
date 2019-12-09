package com.xiaojianjun.wanandroid.ui.detail

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.ui.base.BaseViewModel
import com.xiaojianjun.wanandroid.ui.common.CollectRepository

/**
 * Created by xiaojianjun on 2019-11-18.
 */
class DetailViewModel : BaseViewModel() {

    private val detailRepository by lazy { DetailRepository() }
    private val collectRepository by lazy { CollectRepository() }
    val collect = MutableLiveData<Boolean>()

    fun collect(id: Int) {
        launch(
            block = {
                collectRepository.collect(id)
                // 收藏成功，更新userInfo
                userRepository.updateUserInfo(userRepository.getUserInfo()!!.apply {
                    if (!collectIds.contains(id)) collectIds.add(id)
                })
                collect.value = true
            },
            error = {
                collect.value = false
            }
        )
    }

    fun uncollect(id: Int) {
        launch(
            block = {
                collectRepository.uncollect(id)
                // 取消收藏成功，更新userInfo
                userRepository.updateUserInfo(userRepository.getUserInfo()!!.apply {
                    if (collectIds.contains(id)) collectIds.remove(id)
                })
                collect.value = false
            },
            error = {
                collect.value = true
            }
        )
    }

    fun updateCollectStatus(id: Int) {
        collect.value = if (userRepository.isLogin()) {
            userRepository.getUserInfo()!!.collectIds.contains(id)
        } else {
            false
        }
    }

    fun saveReadHistory(article: Article) {
        launch(block = { detailRepository.saveReadHistory(article) })
    }
}