package com.xiaojianjun.wanandroid.ui.compose.search

import com.xiaojianjun.wanandroid.common.bus.AppEvents
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.bean.HotWord
import com.xiaojianjun.wanandroid.model.repository.CollectRepository
import com.xiaojianjun.wanandroid.model.repository.SearchHistoryRepository
import com.xiaojianjun.wanandroid.model.repository.SearchResultRepository
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.ui.compose.common.ComposeBaseViewModel
import com.xiaojianjun.wanandroid.ui.compose.home.HomeLoadMoreStatus
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SearchUiState(
    val input: String = "",
    val hotWords: List<HotWord> = emptyList(),
    val histories: List<String> = emptyList(),
    val articles: List<Article> = emptyList(),
    val resultVisible: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val showReload: Boolean = false,
    val showEmpty: Boolean = false,
    val loadMoreStatus: HomeLoadMoreStatus = HomeLoadMoreStatus.Complete,
)

sealed interface SearchEvent {
    data object OpenLoginForCollect : SearchEvent
}

class SearchComposeViewModel(
    private val historyRepository: SearchHistoryRepository,
    private val resultRepository: SearchResultRepository,
    private val collectRepository: CollectRepository,
) : ComposeBaseViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState = _uiState.asStateFlow()

    private val _searchEvents = MutableSharedFlow<SearchEvent>()
    val searchEvents = _searchEvents.asSharedFlow()

    private var currentKeywords = ""
    private var page = 0

    init {
        loadHistory()
        loadHotWords()
    }

    fun setInput(input: String) {
        _uiState.update { it.copy(input = input) }
    }

    fun clearInput() {
        _uiState.update { it.copy(input = "") }
    }

    fun hideResult() {
        _uiState.update { it.copy(resultVisible = false) }
    }

    fun search(keywords: String = _uiState.value.input) {
        val trimmedKeywords = keywords.trim()
        if (trimmedKeywords.isEmpty()) return
        addSearchHistory(trimmedKeywords)
        launchFlow(
            block = {
                if (currentKeywords != trimmedKeywords) {
                    currentKeywords = trimmedKeywords
                    page = 0
                    _uiState.update {
                        it.copy(
                            input = trimmedKeywords,
                            articles = emptyList(),
                            resultVisible = true,
                        )
                    }
                }
                _uiState.update {
                    it.copy(
                        isRefreshing = true,
                        showReload = false,
                        showEmpty = false,
                        resultVisible = true,
                    )
                }
                val pagination = resultRepository.search(trimmedKeywords, 0)
                page = pagination.curPage
                _uiState.update {
                    it.copy(
                        articles = pagination.datas,
                        isRefreshing = false,
                        showEmpty = pagination.datas.isEmpty(),
                        loadMoreStatus = pagination.toLoadMoreStatus(),
                    )
                }
            },
            error = {
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        showReload = it.articles.isEmpty(),
                    )
                }
            },
        )
    }

    fun loadMore() {
        val state = _uiState.value
        if (
            currentKeywords.isEmpty() ||
            state.isLoadingMore ||
            state.loadMoreStatus == HomeLoadMoreStatus.End
        ) {
            return
        }
        launchFlow(
            block = {
                _uiState.update {
                    it.copy(isLoadingMore = true, loadMoreStatus = HomeLoadMoreStatus.Loading)
                }
                val pagination = resultRepository.search(currentKeywords, page)
                page = pagination.curPage
                _uiState.update {
                    it.copy(
                        articles = it.articles + pagination.datas,
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

    fun deleteSearchHistory(words: String) {
        historyRepository.deleteSearchHistory(words)
        loadHistory()
    }

    fun updateListCollectState() {
        val articles = _uiState.value.articles
        if (articles.isEmpty()) return
        val collectIds = UserInfoStore.getUserInfo()?.collectIds.orEmpty()
        _uiState.update {
            it.copy(
                articles = articles.map { article ->
                    article.copy(collect = isLogin() && collectIds.contains(article.id))
                },
            )
        }
    }

    fun updateItemCollectState(target: Pair<Long, Boolean>) {
        _uiState.update { state ->
            state.copy(
                articles = state.articles.map { article ->
                    if (article.id == target.first) article.copy(collect = target.second) else article
                },
            )
        }
    }

    fun onCollectClick(article: Article) {
        launchFlow(block = {
            if (!isLogin()) {
                _searchEvents.emit(SearchEvent.OpenLoginForCollect)
                return@launchFlow
            }

            val targetCollect = !article.collect
            updateItemCollectState(article.id to targetCollect)
            if (targetCollect) {
                collectRepository.collect(article.id)
                UserInfoStore.addCollectId(article.id)
            } else {
                collectRepository.uncollect(article.id)
                UserInfoStore.removeCollectId(article.id)
            }
            AppEvents.collectionChanged(article.id to targetCollect)
        })
    }

    private fun loadHotWords() {
        launchFlow(
            block = {
                val hotWords = historyRepository.getHotSearch()
                _uiState.update { it.copy(hotWords = hotWords) }
            },
            showError = false,
        )
    }

    private fun loadHistory() {
        _uiState.update { it.copy(histories = historyRepository.getSearchHistory()) }
    }

    private fun addSearchHistory(searchWords: String) {
        historyRepository.saveSearchHistory(searchWords)
        loadHistory()
    }
}

private fun <T> com.xiaojianjun.wanandroid.model.bean.Pagination<T>.toLoadMoreStatus(): HomeLoadMoreStatus {
    return if (offset >= total) HomeLoadMoreStatus.End else HomeLoadMoreStatus.Complete
}
