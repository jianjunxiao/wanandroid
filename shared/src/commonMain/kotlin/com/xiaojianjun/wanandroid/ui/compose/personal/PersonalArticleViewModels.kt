package com.xiaojianjun.wanandroid.ui.compose.personal

import com.xiaojianjun.wanandroid.common.bus.AppEvents
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.repository.CollectRepository
import com.xiaojianjun.wanandroid.model.repository.CollectionRepository
import com.xiaojianjun.wanandroid.model.repository.HistoryRepository
import com.xiaojianjun.wanandroid.model.repository.SharedRepository
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.ui.compose.common.ComposeBaseViewModel
import com.xiaojianjun.wanandroid.ui.compose.home.HomeLoadMoreStatus
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PersonalArticleUiState(
    val articles: List<Article> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val showReload: Boolean = false,
    val showEmpty: Boolean = false,
    val loadMoreStatus: HomeLoadMoreStatus = HomeLoadMoreStatus.Complete,
)

sealed interface PersonalArticleEvent {
    data object OpenLoginForCollect : PersonalArticleEvent
}

abstract class BasePersonalArticleViewModel(
    protected val collectRepository: CollectRepository,
) : ComposeBaseViewModel() {
    protected val _uiState = MutableStateFlow(PersonalArticleUiState())
    val uiState = _uiState.asStateFlow()

    private val _articleEvents = MutableSharedFlow<PersonalArticleEvent>()
    val articleEvents = _articleEvents.asSharedFlow()

    abstract fun refresh()

    open fun loadMore() = Unit

    open fun deleteArticle(article: Article) = Unit

    open fun onCollectClick(article: Article) {
        launchFlow(block = {
            if (!isLogin()) {
                _articleEvents.emit(PersonalArticleEvent.OpenLoginForCollect)
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

    fun updateListCollectState() {
        val collectIds = UserInfoStore.getUserInfo()?.collectIds.orEmpty()
        _uiState.update { state ->
            state.copy(
                articles = state.articles.map { article ->
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

    protected fun removeArticleById(id: Long) {
        _uiState.update { state ->
            val articles = state.articles.filterNot { it.id == id }
            state.copy(articles = articles, showEmpty = articles.isEmpty())
        }
    }
}

class SharedArticlesComposeViewModel(
    private val repository: SharedRepository,
    collectRepository: CollectRepository,
) : BasePersonalArticleViewModel(collectRepository) {
    private var page = INITIAL_PAGE

    init {
        refresh()
    }

    override fun refresh() {
        launchFlow(
            block = {
                _uiState.update { it.copy(isRefreshing = true, showReload = false, showEmpty = false) }
                val pagination = repository.getSharedArticleList(INITIAL_PAGE).shareArticles
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
                _uiState.update { it.copy(isRefreshing = false, showReload = it.articles.isEmpty()) }
            },
        )
    }

    override fun loadMore() {
        val state = _uiState.value
        if (state.isLoadingMore || state.loadMoreStatus == HomeLoadMoreStatus.End) return
        launchFlow(
            block = {
                _uiState.update { it.copy(isLoadingMore = true, loadMoreStatus = HomeLoadMoreStatus.Loading) }
                val pagination = repository.getSharedArticleList(page + 1).shareArticles
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
                _uiState.update { it.copy(isLoadingMore = false, loadMoreStatus = HomeLoadMoreStatus.Error) }
            },
        )
    }

    override fun deleteArticle(article: Article) {
        removeArticleById(article.id)
        launchFlow(block = { repository.deleteShared(article.id) }, showError = false)
    }

    private companion object {
        const val INITIAL_PAGE = 1
    }
}

class CollectionComposeViewModel(
    private val repository: CollectionRepository,
    collectRepository: CollectRepository,
) : BasePersonalArticleViewModel(collectRepository) {
    private var page = INITIAL_PAGE

    init {
        refresh()
    }

    override fun refresh() {
        launchFlow(
            block = {
                _uiState.update { it.copy(isRefreshing = true, showReload = false, showEmpty = false) }
                val pagination = repository.getCollectionList(INITIAL_PAGE)
                page = pagination.curPage
                val articles = pagination.datas.map { it.copy(collect = true) }
                _uiState.update {
                    it.copy(
                        articles = articles,
                        isRefreshing = false,
                        showEmpty = articles.isEmpty(),
                        loadMoreStatus = pagination.toLoadMoreStatus(),
                    )
                }
            },
            error = {
                _uiState.update { it.copy(isRefreshing = false, showReload = it.articles.isEmpty()) }
            },
        )
    }

    override fun loadMore() {
        val state = _uiState.value
        if (state.isLoadingMore || state.loadMoreStatus == HomeLoadMoreStatus.End) return
        launchFlow(
            block = {
                _uiState.update { it.copy(isLoadingMore = true, loadMoreStatus = HomeLoadMoreStatus.Loading) }
                val pagination = repository.getCollectionList(page)
                page = pagination.curPage
                val articles = pagination.datas.map { it.copy(collect = true) }
                _uiState.update {
                    it.copy(
                        articles = it.articles + articles,
                        isLoadingMore = false,
                        loadMoreStatus = pagination.toLoadMoreStatus(),
                    )
                }
            },
            error = {
                _uiState.update { it.copy(isLoadingMore = false, loadMoreStatus = HomeLoadMoreStatus.Error) }
            },
        )
    }

    override fun onCollectClick(article: Article) {
        val originId = article.originId
        removeCollectedArticle(originId)
        launchFlow(
            block = {
                collectRepository.uncollect(originId)
                UserInfoStore.removeCollectId(originId)
                AppEvents.collectionChanged(originId to false)
            },
            showError = false,
        )
    }

    fun onCollectUpdated(id: Long, collect: Boolean) {
        if (collect) {
            refresh()
        } else {
            removeCollectedArticle(id)
        }
    }

    private fun removeCollectedArticle(originId: Long) {
        _uiState.update { state ->
            val articles = state.articles.filterNot { it.originId == originId }
            state.copy(articles = articles, showEmpty = articles.isEmpty())
        }
    }

    private companion object {
        const val INITIAL_PAGE = 0
    }
}

class HistoryComposeViewModel(
    private val repository: HistoryRepository,
    collectRepository: CollectRepository,
) : BasePersonalArticleViewModel(collectRepository) {
    init {
        refresh()
    }

    override fun refresh() {
        launchFlow(block = {
            val collectIds = UserInfoStore.getUserInfo()?.collectIds.orEmpty()
            val articles = repository.getReadHistory().map { article ->
                article.copy(collect = isLogin() && collectIds.contains(article.id))
            }
            _uiState.update {
                it.copy(
                    articles = articles,
                    showEmpty = articles.isEmpty(),
                    showReload = false,
                )
            }
        })
    }

    override fun deleteArticle(article: Article) {
        removeArticleById(article.id)
        launchFlow(block = { repository.deleteHistory(article) }, showError = false)
    }
}

private fun <T> com.xiaojianjun.wanandroid.model.bean.Pagination<T>.toLoadMoreStatus(): HomeLoadMoreStatus {
    return if (offset >= total) HomeLoadMoreStatus.End else HomeLoadMoreStatus.Complete
}
