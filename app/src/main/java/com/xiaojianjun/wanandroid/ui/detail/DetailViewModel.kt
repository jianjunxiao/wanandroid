package com.xiaojianjun.wanandroid.ui.detail

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.base.BaseViewModel
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.model.store.isLogin
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
                UserInfoStore.addCollectId(id)
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
                UserInfoStore.removeCollectId(id)
                collect.value = false
            },
            error = {
                collect.value = true
            }
        )
    }

    fun updateCollectStatus(id: Int) {
        collect.value = if (isLogin()) {
            UserInfoStore.getUserInfo()?.collectIds?.contains(id)
        } else {
            false
        }
    }

    fun saveReadHistory(article: Article) {
        launch(block = { detailRepository.saveReadHistory(article) })
    }
}