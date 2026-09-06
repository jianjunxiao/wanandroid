package com.xiaojianjun.wanandroid.ui.compose.points

import com.xiaojianjun.wanandroid.model.bean.PointRank
import com.xiaojianjun.wanandroid.model.bean.PointRecord
import com.xiaojianjun.wanandroid.ui.compose.common.ComposeBaseViewModel
import com.xiaojianjun.wanandroid.ui.compose.home.HomeLoadMoreStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MinePointsUiState(
    val totalPoints: PointRank? = null,
    val records: List<PointRecord> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val showReload: Boolean = false,
    val loadMoreStatus: HomeLoadMoreStatus = HomeLoadMoreStatus.Complete,
)

class MinePointsComposeViewModel : ComposeBaseViewModel() {
    private val repository = MinePointsRepository()
    private val _uiState = MutableStateFlow(MinePointsUiState())
    val uiState = _uiState.asStateFlow()

    private var page = INITIAL_PAGE

    init {
        refresh()
    }

    fun refresh() {
        launchFlow(
            block = {
                _uiState.update { it.copy(isRefreshing = true, showReload = false) }
                val points = repository.getMyPoints()
                val pagination = repository.getPointsRecord(INITIAL_PAGE)
                page = pagination.curPage
                _uiState.update {
                    it.copy(
                        totalPoints = points,
                        records = pagination.datas,
                        isRefreshing = false,
                        loadMoreStatus = pagination.toLoadMoreStatus(),
                    )
                }
            },
            error = {
                _uiState.update {
                    it.copy(isRefreshing = false, showReload = it.totalPoints == null && it.records.isEmpty())
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
                val pagination = repository.getPointsRecord(page + 1)
                page = pagination.curPage
                _uiState.update {
                    it.copy(
                        records = it.records + pagination.datas,
                        isLoadingMore = false,
                        loadMoreStatus = pagination.toLoadMoreStatus(),
                    )
                }
            },
            error = {
                _uiState.update {
                    it.copy(isLoadingMore = false, loadMoreStatus = HomeLoadMoreStatus.Error)
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
