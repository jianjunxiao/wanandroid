package com.xiaojianjun.wanandroid.ui.points.rank

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.common.loadmore.LoadMoreStatus
import com.xiaojianjun.wanandroid.model.bean.PointRank
import com.xiaojianjun.wanandroid.ui.base.BaseViewModel

/**
 * Created by xiaojianjun on 2019-12-02.
 */
class PointsRankViewModel : BaseViewModel() {
    companion object {
        const val INITIAL_PAGE = 1
    }

    private val pointsRankRespository by lazy { PointsRankRespository() }

    val pointsRank = MutableLiveData<MutableList<PointRank>>()
    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    private var page = INITIAL_PAGE

    fun refreshData() {
        refreshStatus.value = true
        reloadStatus.value = false
        launch(
            block = {
                val pagination = pointsRankRespository.getPointsRank(INITIAL_PAGE)
                page = pagination.curPage
                pointsRank.value = pagination.datas.toMutableList()
                refreshStatus.value = false
            },
            error = {
                reloadStatus.value = page == INITIAL_PAGE
            }
        )
    }

    fun loadMoreData() {
        loadMoreStatus.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val pagination = pointsRankRespository.getPointsRank(page + 1)
                page = pagination.curPage
                pointsRank.value?.addAll(pagination.datas)
                loadMoreStatus.value = if (pagination.offset >= pagination.total) {
                    LoadMoreStatus.END
                } else {
                    LoadMoreStatus.COMPLETED
                }
            },
            error = { loadMoreStatus.value = LoadMoreStatus.ERROR }
        )
    }
}