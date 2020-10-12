package com.xiaojianjun.wanandroid.ui.main.system.pager

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.base.BaseViewModel
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.xiaojianjun.wanandroid.common.loadmore.LoadMoreStatus
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.ui.common.CollectRepository
import kotlinx.coroutines.Job

/**
 * Created by xiaojianjun on 2019-11-16.
 */
class SystemPagerViewModel : BaseViewModel() {

    companion object {
        const val INITIAL_PAGE = 0
    }

    private val systemPagerRepository by lazy { SystemPagerRepository() }
    private val collectRepository by lazy { CollectRepository() }

    val articleList = MutableLiveData<MutableList<Article>>()
    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    private var page = INITIAL_PAGE
    private var id: Int = -1
    private var refreshJob: Job? = null

    fun refreshArticleList(cid: Int) {
        if (cid != id) {
            cancelJob(refreshJob)
            id = cid
            articleList.value = mutableListOf()
        }
        refreshJob = launch(
            block = {
                refreshStatus.value = true
                reloadStatus.value = false
                val pagination = systemPagerRepository.getArticleListByCid(INITIAL_PAGE, cid)
                page = pagination.curPage
                articleList.value = pagination.datas
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = articleList.value?.isEmpty()
            }
        )
    }

    fun loadMoreArticleList(cid: Int) {
        launch(
            block = {
                loadMoreStatus.value = LoadMoreStatus.LOADING
                val pagination = systemPagerRepository.getArticleListByCid(page, cid)
                page = pagination.curPage

                val currentList = articleList.value ?: mutableListOf()
                currentList.addAll(pagination.datas)

                articleList.value = currentList
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

    fun collect(id: Long) {
        launch(
            block = {
                collectRepository.collect(id)
                UserInfoStore.addCollectId(id)
                updateListCollectState()
                Bus.post(USER_COLLECT_UPDATED, id to true)
            },
            error = {
                updateListCollectState()
            }
        )
    }

    fun uncollect(id: Long) {
        launch(
            block = {
                collectRepository.uncollect(id)
                UserInfoStore.removeCollectId(id)
                updateListCollectState()
                Bus.post(USER_COLLECT_UPDATED, id to false)
            },
            error = {
                updateListCollectState()
            }
        )
    }

    /**
     * 更新列表收藏状态
     */
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

    /**
     * 更新Item的收藏状态
     */
    fun updateItemCollectState(target: Pair<Long, Boolean>) {
        val list = articleList.value
        val item = list?.find { it.id == target.first } ?: return
        item.collect = target.second
        articleList.value = list
    }
}