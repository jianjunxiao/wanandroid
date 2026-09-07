package com.xiaojianjun.wanandroid.ui.compose.home

import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.bean.Category
import com.xiaojianjun.wanandroid.model.repository.CollectRepository
import com.xiaojianjun.wanandroid.model.repository.LatestRepository
import com.xiaojianjun.wanandroid.model.repository.PlazaRepository
import com.xiaojianjun.wanandroid.model.repository.ProjectRepository
import com.xiaojianjun.wanandroid.model.repository.WechatRepository
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.ui.compose.common.ComposeBaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class HomeArticleUiState(
    val articles: List<Article> = emptyList(),
    val categories: List<Category> = emptyList(),
    val checkedCategory: Int = 0,
    val isRefreshing: Boolean = false,
    val isLoadingMore: Boolean = false,
    val showReload: Boolean = false,
    val showListReload: Boolean = false,
    val loadMoreStatus: HomeLoadMoreStatus = HomeLoadMoreStatus.Complete,
    val useSimpleItem: Boolean = false,
)

sealed interface HomeArticleEvent {
    data class OpenLoginForCollect(val articleId: Long) : HomeArticleEvent
}

abstract class BaseHomeArticleViewModel(
    protected val collectRepository: CollectRepository,
) : ComposeBaseViewModel() {
    protected val _uiState = MutableStateFlow(HomeArticleUiState())
    val uiState = _uiState.asStateFlow()

    private val _articleEvents = MutableSharedFlow<HomeArticleEvent>()
    val articleEvents = _articleEvents.asSharedFlow()

    abstract fun refresh()

    abstract fun loadMore()

    open fun selectCategory(position: Int) = Unit

    fun onCollectClick(article: Article) {
        launchFlow(block = {
            if (!isLogin()) {
                _articleEvents.emit(HomeArticleEvent.OpenLoginForCollect(article.id))
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

    protected fun updateCollectState(id: Long, collect: Boolean) {
        _uiState.update { state ->
            state.copy(
                articles = state.articles.map { article ->
                    if (article.id == id) {
                        article.copy(collect = collect)
                    } else {
                        article
                    }
                },
            )
        }
    }
}

class LatestComposeViewModel(
    private val repository: LatestRepository,
    collectRepository: CollectRepository,
) : BaseHomeArticleViewModel(collectRepository) {
    private var page = 0

    init {
        refresh()
    }

    override fun refresh() {
        launchFlow(
            block = {
                _uiState.update { it.copy(isRefreshing = true, showReload = false) }
                val pagination = repository.getProjectList(0)
                page = pagination.curPage
                _uiState.update {
                    it.copy(
                        articles = pagination.datas,
                        isRefreshing = false,
                        loadMoreStatus = pagination.toLoadMoreStatus(),
                    )
                }
            },
            error = {
                _uiState.update {
                    it.copy(isRefreshing = false, showReload = it.articles.isEmpty())
                }
            },
        )
    }

    override fun loadMore() {
        if (_uiState.value.isLoadingMore || _uiState.value.loadMoreStatus == HomeLoadMoreStatus.End) return
        launchFlow(
            block = {
                _uiState.update { it.copy(isLoadingMore = true, loadMoreStatus = HomeLoadMoreStatus.Loading) }
                val pagination = repository.getProjectList(page)
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
}

class PlazaComposeViewModel(
    private val repository: PlazaRepository,
    collectRepository: CollectRepository,
) : BaseHomeArticleViewModel(collectRepository) {
    private var page = 0

    init {
        _uiState.update { it.copy(useSimpleItem = true) }
        refresh()
    }

    override fun refresh() {
        launchFlow(
            block = {
                _uiState.update { it.copy(isRefreshing = true, showReload = false) }
                val pagination = repository.getUserArticleList(0)
                page = pagination.curPage
                _uiState.update {
                    it.copy(
                        articles = pagination.datas,
                        isRefreshing = false,
                        loadMoreStatus = pagination.toLoadMoreStatus(),
                    )
                }
            },
            error = {
                _uiState.update {
                    it.copy(isRefreshing = false, showReload = it.articles.isEmpty())
                }
            },
        )
    }

    override fun loadMore() {
        if (_uiState.value.isLoadingMore || _uiState.value.loadMoreStatus == HomeLoadMoreStatus.End) return
        launchFlow(
            block = {
                _uiState.update { it.copy(isLoadingMore = true, loadMoreStatus = HomeLoadMoreStatus.Loading) }
                val pagination = repository.getUserArticleList(page)
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
}

class ProjectComposeViewModel(
    private val repository: ProjectRepository,
    collectRepository: CollectRepository,
) : BaseHomeArticleViewModel(collectRepository) {
    private var page = 2

    init {
        refresh()
    }

    override fun refresh() {
        launchFlow(
            block = {
                _uiState.update { it.copy(isRefreshing = true, showReload = false) }
                val categories = repository.getProjectCategories()
                val checked = 0
                val pagination = repository.getProjectListByCid(1, categories[checked].id)
                page = pagination.curPage
                _uiState.update {
                    it.copy(
                        categories = categories,
                        checkedCategory = checked,
                        articles = pagination.datas,
                        isRefreshing = false,
                        loadMoreStatus = pagination.toLoadMoreStatus(),
                    )
                }
            },
            error = {
                _uiState.update {
                    it.copy(isRefreshing = false, showReload = true)
                }
            },
        )
    }

    override fun selectCategory(position: Int) {
        if (position == _uiState.value.checkedCategory) return
        refreshList(position)
    }

    private fun refreshList(position: Int = _uiState.value.checkedCategory) {
        launchFlow(
            block = {
                _uiState.update {
                    it.copy(
                        checkedCategory = position,
                        articles = if (position != it.checkedCategory) emptyList() else it.articles,
                        isRefreshing = true,
                        showListReload = false,
                    )
                }
                val cid = _uiState.value.categories[position].id
                val pagination = repository.getProjectListByCid(1, cid)
                page = pagination.curPage
                _uiState.update {
                    it.copy(
                        articles = pagination.datas,
                        isRefreshing = false,
                        loadMoreStatus = pagination.toLoadMoreStatus(),
                    )
                }
            },
            error = {
                _uiState.update {
                    it.copy(isRefreshing = false, showListReload = it.articles.isEmpty())
                }
            },
        )
    }

    override fun loadMore() {
        if (_uiState.value.isLoadingMore || _uiState.value.loadMoreStatus == HomeLoadMoreStatus.End) return
        launchFlow(
            block = {
                _uiState.update { it.copy(isLoadingMore = true, loadMoreStatus = HomeLoadMoreStatus.Loading) }
                val cid = _uiState.value.categories[_uiState.value.checkedCategory].id
                val pagination = repository.getProjectListByCid(page + 1, cid)
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
}

class WechatComposeViewModel(
    private val repository: WechatRepository,
    collectRepository: CollectRepository,
) : BaseHomeArticleViewModel(collectRepository) {
    private var page = 1

    init {
        _uiState.update { it.copy(useSimpleItem = true) }
        refresh()
    }

    override fun refresh() {
        launchFlow(
            block = {
                _uiState.update { it.copy(isRefreshing = true, showReload = false) }
                val categories = repository.getWechatCategories()
                val checked = 0
                val pagination = repository.getWechatArticleList(1, categories[checked].id)
                page = pagination.curPage
                _uiState.update {
                    it.copy(
                        categories = categories,
                        checkedCategory = checked,
                        articles = pagination.datas,
                        isRefreshing = false,
                        loadMoreStatus = pagination.toLoadMoreStatus(),
                    )
                }
            },
            error = {
                _uiState.update {
                    it.copy(isRefreshing = false, showReload = true)
                }
            },
        )
    }

    override fun selectCategory(position: Int) {
        if (position == _uiState.value.checkedCategory) return
        refreshList(position)
    }

    private fun refreshList(position: Int = _uiState.value.checkedCategory) {
        launchFlow(
            block = {
                _uiState.update {
                    it.copy(
                        checkedCategory = position,
                        articles = if (position != it.checkedCategory) emptyList() else it.articles,
                        isRefreshing = true,
                        showListReload = false,
                    )
                }
                val id = _uiState.value.categories[position].id
                val pagination = repository.getWechatArticleList(1, id)
                page = pagination.curPage
                _uiState.update {
                    it.copy(
                        articles = pagination.datas,
                        isRefreshing = false,
                        loadMoreStatus = pagination.toLoadMoreStatus(),
                    )
                }
            },
            error = {
                _uiState.update {
                    it.copy(isRefreshing = false, showListReload = it.articles.isEmpty())
                }
            },
        )
    }

    override fun loadMore() {
        if (_uiState.value.isLoadingMore || _uiState.value.loadMoreStatus == HomeLoadMoreStatus.End) return
        launchFlow(
            block = {
                _uiState.update { it.copy(isLoadingMore = true, loadMoreStatus = HomeLoadMoreStatus.Loading) }
                val id = _uiState.value.categories[_uiState.value.checkedCategory].id
                val pagination = repository.getWechatArticleList(page + 1, id)
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
}

private fun <T> com.xiaojianjun.wanandroid.model.bean.Pagination<T>.toLoadMoreStatus(): HomeLoadMoreStatus {
    return if (offset >= total) HomeLoadMoreStatus.End else HomeLoadMoreStatus.Complete
}
