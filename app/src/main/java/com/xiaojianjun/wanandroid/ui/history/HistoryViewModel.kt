package com.xiaojianjun.wanandroid.ui.history

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.base.BaseViewModel
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.ui.common.CollectRepository

/**
 * Created by xiaojianjun on 2019-11-29.
 */
class HistoryViewModel : BaseViewModel() {


    private val historyRepository by lazy { HistoryRepository() }
    private val collectRepository by lazy { CollectRepository() }

    val articleList = MutableLiveData<MutableList<Article>>()

    val emptyStatus = MutableLiveData<Boolean>()


    fun getData() {
        launch(
            block = {
                emptyStatus.value = false

                val readHistory = historyRepository.getReadHistory()
                val collectIds = UserInfoStore.getUserInfo()?.collectIds ?: emptyList<Int>()
                readHistory.forEach { it.collect = collectIds.contains(it.id) }

                articleList.value = readHistory.toMutableList()
                emptyStatus.value = readHistory.isEmpty()
            }
        )
    }

    fun collect(id: Long) {
        launch(
            block = {
                collectRepository.collect(id)
                UserInfoStore.addCollectId(id)
                updateItemCollectState(id to true)
                Bus.post(USER_COLLECT_UPDATED, id to true)
            },
            error = {
                updateItemCollectState(id to false)
            }
        )
    }

    fun uncollect(id: Long) {
        launch(
            block = {
                collectRepository.uncollect(id)
                UserInfoStore.removeCollectId(id)
                updateItemCollectState(id to false)
                Bus.post(USER_COLLECT_UPDATED, id to false)
            },
            error = {
                updateItemCollectState(id to true)
            }
        )
    }

    fun updateListCollectState() {
        val list = articleList.value
        if (list.isNullOrEmpty()) return
        if (isLogin()) {
            val collectIds = UserInfoStore.getUserInfo()?.collectIds ?: return
            list.forEach { it.collect = collectIds.contains(it.id) }
        } else {
            list.forEach { it.collect = false }
        }
        articleList.value = list
    }

    fun updateItemCollectState(target: Pair<Long, Boolean>) {
        val list = articleList.value
        val item = list?.find { it.id == target.first } ?: return
        item.collect = target.second
        articleList.value = list
    }

    fun deleteHistory(article: Article) {
        launch(
            block = { historyRepository.deleteHistory(article) }
        )
    }
}