package com.xiaojianjun.wanandroid.ui.compose.home

import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.repository.CollectRepository
import com.xiaojianjun.wanandroid.model.repository.PopularRepository
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.ui.compose.common.ComposeBaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PopularUiState(
    val articles: List<Article> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val showReload: Boolean = false,
    val loadMoreStatus: HomeLoadMoreStatus = HomeLoadMoreStatus.Complete,
)

sealed interface PopularEvent {
    data class OpenLoginForCollect(val articleId: Long) : PopularEvent
}

class PopularComposeViewModel(
    private val popularRepository: PopularRepository,
    private val collectRepository: CollectRepository,
) : ComposeBaseViewModel() {
    companion object {
        private const val INITIAL_PAGE = 0
    }

    private val _uiState = MutableStateFlow(PopularUiState())
    val uiState = _uiState.asStateFlow()

    private val _popularEvents = MutableSharedFlow<PopularEvent>()
    val popularEvents = _popularEvents.asSharedFlow()

    private var page = INITIAL_PAGE

    init {
        refresh()
    }

    fun refresh() {
        _uiState.update { it.copy(isRefreshing = true, showReload = false) }
        launchFlow(
            block = {
                val topArticles = popularRepository.getTopArticleList().onEach { it.top = true }
                val pagination = popularRepository.getArticleList(INITIAL_PAGE)
                page = pagination.curPage
                _uiState.update {
                    it.copy(
                        articles = topArticles + pagination.datas,
                        isRefreshing = false,
                        showReload = false,
                        loadMoreStatus = pagination.toHomeLoadMoreStatus(),
                    )
                }
            },
            error = {
                _uiState.update { state ->
                    state.copy(
                        isRefreshing = false,
                        showReload = state.articles.isEmpty(),
                    )
                }
            },
        )
    }

    fun loadMore() {
        val state = _uiState.value
        if (state.isLoadingMore || state.loadMoreStatus == HomeLoadMoreStatus.End || state.articles.isEmpty()) return
        _uiState.update { it.copy(isLoadingMore = true, loadMoreStatus = HomeLoadMoreStatus.Loading) }
        launchFlow(
            block = {
                val pagination = popularRepository.getArticleList(page)
                page = pagination.curPage
                _uiState.update {
                    it.copy(
                        articles = it.articles + pagination.datas,
                        isLoadingMore = false,
                        loadMoreStatus = pagination.toHomeLoadMoreStatus(),
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

    fun onCollectClick(article: Article) {
        launchFlow(
            block = {
                if (!isLogin()) {
                    _popularEvents.emit(PopularEvent.OpenLoginForCollect(article.id))
                    return@launchFlow
                }
                setArticleCollect(article.id, !article.collect)
                if (article.collect) {
                    collectRepository.uncollect(article.id)
                    UserInfoStore.removeCollectId(article.id)
                    setArticleCollect(article.id, false)
                } else {
                    collectRepository.collect(article.id)
                    UserInfoStore.addCollectId(article.id)
                    setArticleCollect(article.id, true)
                }
            },
            error = {
                setArticleCollect(article.id, article.collect)
            },
        )
    }

    fun updateCollectStateFromLogin() {
        val collectIds = UserInfoStore.getUserInfo()?.collectIds.orEmpty()
        _uiState.update { state ->
            state.copy(
                articles = state.articles.map { article ->
                    article.copy(collect = isLogin() && article.id in collectIds)
                },
            )
        }
    }

    private fun setArticleCollect(id: Long, collect: Boolean) {
        _uiState.update { state ->
            state.copy(
                articles = state.articles.map { article ->
                    if (article.id == id) article.copy(collect = collect) else article
                },
            )
        }
    }
}

private fun <T> com.xiaojianjun.wanandroid.model.bean.Pagination<T>.toHomeLoadMoreStatus(): HomeLoadMoreStatus {
    return if (offset >= total) HomeLoadMoreStatus.End else HomeLoadMoreStatus.Complete
}
