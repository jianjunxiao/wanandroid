package com.xiaojianjun.wanandroid.ui.points.mine

import androidx.lifecycle.MutableLiveData
import com.xiaojianjun.wanandroid.common.loadmore.LoadMoreStatus
import com.xiaojianjun.wanandroid.model.bean.PointRank
import com.xiaojianjun.wanandroid.model.bean.PointRecord
import com.xiaojianjun.wanandroid.ui.base.BaseViewModel

/**
 * Created by xiaojianjun on 2019-12-02.
 */
class MinePointsViewModel : BaseViewModel() {
    companion object {
        const val INITIAL_PAGE = 1
    }

    private val minePointsRespository by lazy { MinePointsRespository() }

    val totalPoints = MutableLiveData<PointRank>()
    val pointsList = MutableLiveData<MutableList<PointRecord>>()

    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()
    var page = INITIAL_PAGE

    fun refresh() {
        refreshStatus.value = true
        reloadStatus.value = false
        launch(
            block = {
                val points = minePointsRespository.getMyPoints()
                val pagination = minePointsRespository.getPointsRecord(INITIAL_PAGE)
                page = pagination.curPage
                totalPoints.value = points
                pointsList.value = pagination.datas.toMutableList()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = page == INITIAL_PAGE
            }
        )
    }

    fun loadMoreRecord() {
        loadMoreStatus.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val pagination = minePointsRespository.getPointsRecord(page + 1)
                page = pagination.curPage
                pointsList.value?.addAll(pagination.datas)
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