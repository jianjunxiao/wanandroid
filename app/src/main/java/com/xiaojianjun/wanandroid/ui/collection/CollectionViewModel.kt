package com.xiaojianjun.wanandroid.ui.collection

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.base.BaseViewModel
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.xiaojianjun.wanandroid.common.loadmore.LoadMoreStatus
import com.xiaojianjun.wanandroid.ext.concat
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.ui.common.CollectRepository

class CollectionViewModel : BaseViewModel() {

    companion object {
        const val INITIAL_PAGE = 0
    }

    private val collectionRepository by lazy { CollectionRepository() }
    private val collectRepository by lazy { CollectRepository() }

    val articleList = MutableLiveData<MutableList<Article>>()

    val refreshStatus = MutableLiveData<Boolean>()
    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val reloadStatus = MutableLiveData<Boolean>()
    val emptyStatus = MutableLiveData<Boolean>()

    private var page = INITIAL_PAGE

    fun refresh() {
        launch(
            block = {
                refreshStatus.value = true
                emptyStatus.value = false
                reloadStatus.value = false

                val pagination = collectionRepository.getCollectionList(INITIAL_PAGE)
                pagination.datas.forEach { it.collect = true }
                page = pagination.curPage
                articleList.value = pagination.datas
                emptyStatus.value = pagination.datas.isEmpty()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = page == INITIAL_PAGE
            }
        )
    }

    fun loadMore() {
        launch(
            block = {
                loadMoreStatus.value = LoadMoreStatus.LOADING

                val pagination = collectionRepository.getCollectionList(page)
                pagination.datas.forEach { it.collect = true }
                page = pagination.curPage
                articleList.value = articleList.value.concat(pagination.datas)

                loadMoreStatus.value = if (pagination.offset >= pagination.total) {
                    LoadMoreStatus.END
                } else {
                    LoadMoreStatus.COMPLETED
                }
            },
            error = {
                loadMoreStatus.value = LoadMoreStatus.ERROR
            }
        )
    }

    fun uncollect(id: Long) {
        launch(
            block = {
                collectRepository.uncollect(id)
                UserInfoStore.removeCollectId(id)
                Bus.post(USER_COLLECT_UPDATED, id to false)
            }
        )
    }
}
