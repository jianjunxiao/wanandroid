package com.xiaojianjun.wanandroid.ui.compose.personal

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xiaojianjun.wanandroid.common.bus.AppEvents
import com.xiaojianjun.wanandroid.di.appViewModel
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.resources.*
import com.xiaojianjun.wanandroid.ui.compose.components.ArticleCard
import com.xiaojianjun.wanandroid.ui.compose.components.ReloadContent
import com.xiaojianjun.wanandroid.ui.compose.components.ToolbarBottomShadow
import com.xiaojianjun.wanandroid.ui.compose.home.HomeLoadMoreStatus
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanNavigator
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute
import com.xiaojianjun.wanandroid.ui.compose.navigation.toArticleDetailRoute
import com.xiaojianjun.wanandroid.ui.compose.theme.WanTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun SharedArticlesScreen(
    navigator: WanNavigator,
    modifier: Modifier = Modifier,
    viewModel: SharedArticlesComposeViewModel = appViewModel { SharedArticlesComposeViewModel(sharedRepository, collectRepository) },
) {
    PersonalArticleRoute(
        title = stringResource(Res.string.my_share),
        navigator = navigator,
        viewModel = viewModel,
        modifier = modifier,
        showAdd = true,
        onAddClick = { navigator.navigate(WanRoute.ShareArticle) },
        deleteMessage = stringResource(Res.string.confirm_delete_shared),
    )
}

@Composable
fun CollectionScreen(
    navigator: WanNavigator,
    modifier: Modifier = Modifier,
    viewModel: CollectionComposeViewModel = appViewModel { CollectionComposeViewModel(collectionRepository, collectRepository) },
) {
    PersonalArticleRoute(
        title = stringResource(Res.string.my_collect),
        navigator = navigator,
        viewModel = viewModel,
        modifier = modifier,
        observeCollectionUpdates = true,
    )
}

@Composable
fun HistoryScreen(
    navigator: WanNavigator,
    modifier: Modifier = Modifier,
    viewModel: HistoryComposeViewModel = appViewModel { HistoryComposeViewModel(historyRepository, collectRepository) },
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, viewModel) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refresh()
            }
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }
    PersonalArticleRoute(
        title = stringResource(Res.string.my_view_history),
        navigator = navigator,
        viewModel = viewModel,
        modifier = modifier,
        pullRefreshEnabled = false,
        deleteMessage = stringResource(Res.string.confirm_delete_history),
    )
}

@Composable
private fun PersonalArticleRoute(
    title: String,
    navigator: WanNavigator,
    viewModel: BasePersonalArticleViewModel,
    modifier: Modifier = Modifier,
    showAdd: Boolean = false,
    onAddClick: () -> Unit = {},
    pullRefreshEnabled: Boolean = true,
    deleteMessage: String? = null,
    observeCollectionUpdates: Boolean = false,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current
    var pendingDeleteArticle by remember { mutableStateOf<Article?>(null) }

    LaunchedEffect(viewModel, navigator) {
        viewModel.articleEvents.collect { event ->
            when (event) {
                PersonalArticleEvent.OpenLoginForCollect -> navigator.navigate(WanRoute.Login)
            }
        }
    }

    LaunchedEffect(viewModel) {
        AppEvents.loginChanges.collect { viewModel.updateListCollectState() }
    }
    LaunchedEffect(viewModel, observeCollectionUpdates) {
        AppEvents.collectionChanges.collect {
            if (observeCollectionUpdates && viewModel is CollectionComposeViewModel) {
                viewModel.onCollectUpdated(it.first, it.second)
            } else {
                viewModel.updateItemCollectState(it)
            }
        }
    }

    PersonalArticleContent(
        title = title,
        state = state,
        onBackClick = { navigator.goBack() },
        onAddClick = onAddClick,
        showAdd = showAdd,
        onReload = viewModel::refresh,
        onLoadMore = viewModel::loadMore,
        onArticleClick = { navigator.openArticle(it) },
        onCollectClick = viewModel::onCollectClick,
        onArticleLongClick = if (deleteMessage == null) {
            null
        } else {
            { article -> pendingDeleteArticle = article }
        },
        pullRefreshEnabled = pullRefreshEnabled,
        modifier = modifier,
    )

    val deleteArticle = pendingDeleteArticle
    if (deleteArticle != null && deleteMessage != null) {
        ConfirmDeleteDialog(
            message = deleteMessage,
            onConfirm = {
                viewModel.deleteArticle(deleteArticle)
                pendingDeleteArticle = null
            },
            onDismiss = { pendingDeleteArticle = null },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun PersonalArticleContent(
    title: String,
    state: PersonalArticleUiState,
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
    showAdd: Boolean,
    onReload: () -> Unit,
    onLoadMore: () -> Unit,
    onArticleClick: (Article) -> Unit,
    onCollectClick: (Article) -> Unit,
    onArticleLongClick: ((Article) -> Unit)?,
    pullRefreshEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WanTheme.colors.backgroundSecondary),
    ) {
        PersonalToolbar(
            title = title,
            onBackClick = onBackClick,
            showAdd = showAdd,
            onAddClick = onAddClick,
        )
        when {
            state.showReload -> ReloadContent(onReload = onReload, modifier = Modifier.weight(1f))
            state.showEmpty -> EmptyPersonalContent(modifier = Modifier.weight(1f))
            else -> {
                val shouldLoadMore by remember(state.articles.size, state.loadMoreStatus) {
                    derivedStateOf {
                        val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                        state.loadMoreStatus == HomeLoadMoreStatus.Complete &&
                            lastVisible != null &&
                            lastVisible >= state.articles.lastIndex - 2
                    }
                }
                LaunchedEffect(shouldLoadMore) {
                    if (shouldLoadMore) onLoadMore()
                }

                val refreshState = rememberPullToRefreshState()
                PullToRefreshBox(
                    isRefreshing = pullRefreshEnabled && state.isRefreshing,
                    onRefresh = { if (pullRefreshEnabled) onReload() },
                    state = refreshState,
                    indicator = {
                        if (pullRefreshEnabled) {
                            PullToRefreshDefaults.Indicator(
                                state = refreshState,
                                isRefreshing = state.isRefreshing,
                                color = WanTheme.colors.textPrimary,
                                containerColor = WanTheme.colors.backgroundPrimary,
                                modifier = Modifier.align(Alignment.TopCenter),
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(WanTheme.colors.backgroundSecondary),
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(WanTheme.colors.backgroundSecondary),
                        state = listState,
                    ) {
                        items(
                            count = state.articles.size,
                            key = { state.articles[it].id },
                        ) { index ->
                            val article = state.articles[index]
                            ArticleCard(
                                article = article,
                                onClick = { onArticleClick(article) },
                                onCollectClick = { onCollectClick(article) },
                                onLongClick = onArticleLongClick?.let { callback -> { callback(article) } },
                            )
                        }
                        if (state.loadMoreStatus.visible) {
                            item {
                                PersonalLoadMoreFooter(
                                    status = state.loadMoreStatus,
                                    onRetry = onLoadMore,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PersonalToolbar(
    title: String,
    onBackClick: () -> Unit,
    showAdd: Boolean,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .shadow(2.dp)
            .background(WanTheme.colors.backgroundPrimary),
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 4.dp, end = 4.dp)
                .size(48.dp),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_back_black_24dp),
                contentDescription = null,
                tint = WanTheme.colors.textPrimary,
                modifier = Modifier.size(24.dp),
            )
        }
        Text(
            text = title,
            color = WanTheme.colors.textPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Center),
        )
        if (showAdd) {
            IconButton(
                onClick = onAddClick,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 5.dp)
                    .size(48.dp),
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_add_black_24dp),
                    contentDescription = null,
                    tint = WanTheme.colors.textPrimary,
                    modifier = Modifier.size(26.dp),
                )
            }
        }
        ToolbarBottomShadow(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun EmptyPersonalContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WanTheme.colors.backgroundPrimary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_info_outline_black_24dp),
            contentDescription = null,
            tint = WanTheme.colors.textThird,
            modifier = Modifier.size(48.dp),
        )
        Text(
            text = stringResource(Res.string.no_data),
            color = WanTheme.colors.textThird,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
}

@Composable
private fun PersonalLoadMoreFooter(
    status: HomeLoadMoreStatus,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(bottom = 8.dp)
            .clickable(enabled = status == HomeLoadMoreStatus.Error, onClick = onRetry),
        contentAlignment = Alignment.Center,
    ) {
        when (status) {
            HomeLoadMoreStatus.Loading -> {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(
                        color = WanTheme.colors.textPrimary,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(Res.string.loading),
                        color = WanTheme.colors.textPrimary,
                        fontSize = 12.sp,
                    )
                }
            }
            HomeLoadMoreStatus.Error -> {
                Text(
                    text = stringResource(Res.string.load_failed),
                    color = WanTheme.colors.textPrimary,
                    fontSize = 14.sp,
                )
            }
            HomeLoadMoreStatus.End -> {
                Text(
                    text = stringResource(Res.string.load_end),
                    color = WanTheme.colors.textThird,
                    fontSize = 14.sp,
                )
            }
            HomeLoadMoreStatus.Complete -> Unit
        }
    }
}

@Composable
private fun ConfirmDeleteDialog(
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = message,
                color = WanTheme.colors.textPrimary,
                fontSize = 14.sp,
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(Res.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(Res.string.cancel))
            }
        },
    )
}

private fun WanNavigator.openArticle(article: Article) {
    navigate(article.toArticleDetailRoute())
}
