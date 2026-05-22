package com.xiaojianjun.wanandroid.ui.compose.points

import com.xiaojianjun.wanandroid.model.bean.PointRank
import com.xiaojianjun.wanandroid.ui.compose.common.ComposeBaseViewModel
import com.xiaojianjun.wanandroid.ui.compose.home.HomeLoadMoreStatus

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PointsRankUiState(
    val ranks: List<PointRank> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val showReload: Boolean = false,
    val loadMoreStatus: HomeLoadMoreStatus = HomeLoadMoreStatus.Complete,
)

class PointsRankComposeViewModel : ComposeBaseViewModel() {
    private val repository = PointsRankRepository()
    private val _uiState = MutableStateFlow(PointsRankUiState())
    val uiState = _uiState.asStateFlow()

    private var page = INITIAL_PAGE

    init {
        refresh()
    }

    fun refresh() {
        launchFlow(
            block = {
                _uiState.update { it.copy(isRefreshing = true, showReload = false) }
                val pagination = repository.getPointsRank(INITIAL_PAGE)
                page = pagination.curPage
                _uiState.update {
                    it.copy(
                        ranks = pagination.datas,
                        isRefreshing = false,
                        loadMoreStatus = pagination.toLoadMoreStatus(),
                    )
                }
            },
            error = {
                _uiState.update {
                    it.copy(isRefreshing = false, showReload = it.ranks.isEmpty())
                }
            },
        )
    }

    fun loadMore() {
        val state = _uiState.value
        if (state.isLoadingMore || state.loadMoreStatus == HomeLoadMoreStatus.End) return
        launchFlow(
            block = {
                _uiState.update { it.copy(isLoadingMore = true, loadMoreStatus = HomeLoadMoreStatus.Loading) }
                val pagination = repository.getPointsRank(page + 1)
                page = pagination.curPage
                _uiState.update {
                    it.copy(
                        ranks = it.ranks + pagination.datas,
                        isLoadingMore = false,
                        loadMoreStatus = pagination.toLoadMoreStatus(),
                    )
                }
            },
            error = {
                _uiState.update {
                    it.copy(
                        isLoadingMore = false,
                        loadMoreStatus = HomeLoadMoreStatus.Error,
                    )
                }
            },
        )
    }

    private companion object {
        const val INITIAL_PAGE = 1
    }
}

private fun <T> com.xiaojianjun.wanandroid.model.bean.Pagination<T>.toLoadMoreStatus(): HomeLoadMoreStatus {
    return if (offset >= total) HomeLoadMoreStatus.End else HomeLoadMoreStatus.Complete
}
