package com.xiaojianjun.wanandroid.ui.compose.system

import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.bean.Category
import com.xiaojianjun.wanandroid.model.bean.Pagination
import com.xiaojianjun.wanandroid.model.repository.CollectRepository
import com.xiaojianjun.wanandroid.model.repository.SystemPagerRepository
import com.xiaojianjun.wanandroid.model.repository.SystemRepository
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.ui.compose.common.ComposeBaseViewModel
import com.xiaojianjun.wanandroid.ui.compose.home.HomeLoadMoreStatus
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SystemUiState(
    val categories: List<Category> = emptyList(),
    val selectedParent: Int = 0,
    val selectedChildren: Map<Int, Int> = emptyMap(),
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val showReload: Boolean = false,
    val showListReload: Boolean = false,
    val loadMoreStatus: HomeLoadMoreStatus = HomeLoadMoreStatus.Complete,
) {
    val children: List<Category>
        get() = categories.getOrNull(selectedParent)?.children.orEmpty()

    val selectedChild: Int
        get() = selectedChildren[selectedParent] ?: 0
}

sealed interface SystemEvent {
    data object OpenLoginForCollect : SystemEvent
}

private data class SystemParentCache(
    val selectedChild: Int = 0,
    val articles: List<Article> = emptyList(),
    val page: Int = 0,
    val loaded: Boolean = false,
    val showListReload: Boolean = false,
    val loadMoreStatus: HomeLoadMoreStatus = HomeLoadMoreStatus.Complete,
)

class SystemComposeViewModel(
    private val systemRepository: SystemRepository,
    private val pagerRepository: SystemPagerRepository,
    private val collectRepository: CollectRepository,
) : ComposeBaseViewModel() {
    private val _uiState = MutableStateFlow(SystemUiState(isLoading = true))
    val uiState = _uiState.asStateFlow()

    private val _systemEvents = MutableSharedFlow<SystemEvent>()
    val systemEvents = _systemEvents.asSharedFlow()

    private val parentCaches = mutableMapOf<Int, SystemParentCache>()
    private var categoryJob: Job? = null
    private var articleRefreshJob: Job? = null

    init {
        refreshCategories()
    }

    fun refreshCategories() {
        categoryJob?.cancel()
        categoryJob = launchFlow(
            block = {
                _uiState.update { it.copy(isLoading = it.categories.isEmpty(), showReload = false) }
                val categories = systemRepository.getArticleCategories()
                    .filter { it.children.isNotEmpty() }
                _uiState.update {
                    it.copy(
                        categories = categories,
                        selectedParent = it.selectedParent.coerceAtMost((categories.size - 1).coerceAtLeast(0)),
                        isLoading = false,
                        showReload = false,
                    )
                }
                refreshArticles(clearList = true)
            },
            error = {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isRefreshing = false,
                        showReload = it.categories.isEmpty(),
                    )
                }
            },
        )
    }

    fun selectParent(index: Int) {
        val state = _uiState.value
        if (index == state.selectedParent || index !in state.categories.indices) return
        val cache = parentCaches[index] ?: SystemParentCache()
        showParentCache(index, cache)
        if (!cache.loaded) {
            refreshArticles(clearList = true)
        }
    }

    fun selectChild(index: Int) {
        val state = _uiState.value
        if (index == state.selectedChild || index !in state.children.indices) return
        parentCaches[state.selectedParent] = SystemParentCache(selectedChild = index)
        _uiState.update {
            it.copy(
                selectedChildren = it.selectedChildren + (it.selectedParent to index),
                articles = emptyList(),
                showListReload = false,
                loadMoreStatus = HomeLoadMoreStatus.Complete,
            )
        }
        refreshArticles(clearList = true)
    }

    fun selectCategory(parentIndex: Int, childIndex: Int) {
        val state = _uiState.value
        val parent = state.categories.getOrNull(parentIndex) ?: return
        if (childIndex !in parent.children.indices) return
        if (parentIndex == state.selectedParent && childIndex == state.selectedChild) return
        val oldCache = parentCaches[parentIndex] ?: SystemParentCache()
        val newCache = if (oldCache.selectedChild == childIndex) {
            oldCache
        } else {
            SystemParentCache(selectedChild = childIndex)
        }
        parentCaches[parentIndex] = newCache
        showParentCache(parentIndex, newCache)
        if (!newCache.loaded) {
            refreshArticles(clearList = true)
        }
    }

    private fun showParentCache(parentIndex: Int, cache: SystemParentCache) {
        _uiState.update {
            it.copy(
                selectedParent = parentIndex,
                selectedChildren = it.selectedChildren + (parentIndex to cache.selectedChild),
                articles = cache.articles,
                isRefreshing = false,
                isLoadingMore = false,
                showListReload = cache.showListReload,
                loadMoreStatus = cache.loadMoreStatus,
            )
        }
    }

    fun refreshArticles(clearList: Boolean = false) {
        val state = _uiState.value
        val parentIndex = state.selectedParent
        val selectedChild = state.selectedChild
        val cid = currentChildId(state) ?: return
        articleRefreshJob?.cancel()
        articleRefreshJob = launchFlow(
            block = {
                _uiState.update {
                    it.copy(
                        articles = if (clearList) emptyList() else it.articles,
                        isRefreshing = true,
                        showListReload = false,
                    )
                }
                val pagination = pagerRepository.getArticleListByCid(0, cid)
                val cache = SystemParentCache(
                    selectedChild = selectedChild,
                    articles = pagination.datas,
                    page = pagination.curPage,
                    loaded = true,
                    loadMoreStatus = pagination.toLoadMoreStatus(),
                )
                parentCaches[parentIndex] = cache
                _uiState.update {
                    if (it.selectedParent == parentIndex && it.selectedChild == selectedChild) {
                        it.copy(
                            articles = cache.articles,
                            isRefreshing = false,
                            showListReload = false,
                            loadMoreStatus = cache.loadMoreStatus,
                        )
                    } else {
                        it
                    }
                }
            },
            error = {
                val cache = SystemParentCache(
                    selectedChild = selectedChild,
                    loaded = true,
                    showListReload = parentCaches[parentIndex].orEmptyArticles() && _uiState.value.categories.isNotEmpty(),
                )
                parentCaches[parentIndex] = cache
                _uiState.update {
                    if (it.selectedParent == parentIndex && it.selectedChild == selectedChild) {
                        it.copy(
                            isRefreshing = false,
                            showListReload = cache.showListReload,
                        )
                    } else {
                        it
                    }
                }
            },
        )
    }

    fun loadMore() {
        val state = _uiState.value
        val parentIndex = state.selectedParent
        val selectedChild = state.selectedChild
        val cache = parentCaches[parentIndex] ?: SystemParentCache(selectedChild = selectedChild)
        val cid = currentChildId(state) ?: return
        if (state.isLoadingMore || state.loadMoreStatus == HomeLoadMoreStatus.End) return
        launchFlow(
            block = {
                _uiState.update { it.copy(isLoadingMore = true, loadMoreStatus = HomeLoadMoreStatus.Loading) }
                val pagination = pagerRepository.getArticleListByCid(cache.page, cid)
                val newCache = cache.copy(
                    articles = cache.articles + pagination.datas,
                    page = pagination.curPage,
                    loaded = true,
                    showListReload = false,
                    loadMoreStatus = pagination.toLoadMoreStatus(),
                )
                parentCaches[parentIndex] = newCache
                _uiState.update {
                    if (it.selectedParent == parentIndex && it.selectedChild == selectedChild) {
                        it.copy(
                            articles = newCache.articles,
                            isLoadingMore = false,
                            loadMoreStatus = newCache.loadMoreStatus,
                        )
                    } else {
                        it
                    }
                }
            },
            error = {
                val newCache = cache.copy(
                    loaded = true,
                    showListReload = false,
                    loadMoreStatus = HomeLoadMoreStatus.Error,
                )
                parentCaches[parentIndex] = newCache
                _uiState.update {
                    if (it.selectedParent == parentIndex && it.selectedChild == selectedChild) {
                        it.copy(
                            isLoadingMore = false,
                            loadMoreStatus = HomeLoadMoreStatus.Error,
                        )
                    } else {
                        it
                    }
                }
            },
        )
    }

    fun onCollectClick(article: Article) {
        launchFlow(block = {
            if (!isLogin()) {
                _systemEvents.emit(SystemEvent.OpenLoginForCollect)
                return@launchFlow
            }

            val targetCollect = !article.collect
            updateCollectState(article.id, targetCollect)
            if (targetCollect) {
                collectRepository.collect(article.id)
                UserInfoStore.addCollectId(article.id)
            } else {
                collectRepository.uncollect(article.id)
                UserInfoStore.removeCollectId(article.id)
            }
        })
    }

    private fun updateCollectState(id: Long, collect: Boolean) {
        parentCaches.putAll(parentCaches.mapValues { (_, cache) ->
            cache.copy(
                articles = cache.articles.map { article ->
                    if (article.id == id) article.copy(collect = collect) else article
                },
            )
        })
        _uiState.update { state ->
            state.copy(
                articles = state.articles.map { article ->
                    if (article.id == id) article.copy(collect = collect) else article
                },
            )
        }
    }

    private fun currentChildId(state: SystemUiState): Int? {
        return state.children.getOrNull(state.selectedChild)?.id
    }
}

private fun <T> Pagination<T>.toLoadMoreStatus(): HomeLoadMoreStatus {
    return if (offset >= total) HomeLoadMoreStatus.End else HomeLoadMoreStatus.Complete
}

private fun SystemParentCache?.orEmptyArticles(): Boolean {
    return this == null || articles.isEmpty()
}
