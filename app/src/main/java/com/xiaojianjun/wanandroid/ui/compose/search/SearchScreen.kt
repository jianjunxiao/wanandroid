package com.xiaojianjun.wanandroid.ui.compose.search

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.bean.HotWord
import com.xiaojianjun.wanandroid.ui.compose.components.ArticleCard
import com.xiaojianjun.wanandroid.ui.compose.components.ReloadContent
import com.xiaojianjun.wanandroid.ui.compose.home.HomeLoadMoreStatus
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanNavigator
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute
import com.xiaojianjun.wanandroid.ui.compose.navigation.toArticleDetailRoute
import com.xiaojianjun.wanandroid.ui.compose.theme.WanAndroidTheme
import com.xiaojianjun.wanandroid.ui.compose.theme.WanTheme

@Composable
fun SearchScreen(
    navigator: WanNavigator,
    initialKeywords: String,
    modifier: Modifier = Modifier,
    viewModel: SearchComposeViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val keyboardController = LocalSoftwareKeyboardController.current
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(initialKeywords) {
        if (initialKeywords.isNotEmpty()) {
            viewModel.setInput(initialKeywords)
            viewModel.search(initialKeywords)
        }
    }

    LaunchedEffect(viewModel, navigator) {
        viewModel.searchEvents.collect { event ->
            when (event) {
                SearchEvent.OpenLoginForCollect -> navigator.navigate(WanRoute.Login)
            }
        }
    }

    DisposableEffect(lifecycleOwner, viewModel) {
        val loginObserver = Observer<Boolean> {
            viewModel.updateListCollectState()
        }
        val collectObserver = Observer<Pair<Long, Boolean>> {
            viewModel.updateItemCollectState(it)
        }
        Bus.observe(USER_LOGIN_STATE_CHANGED, lifecycleOwner, loginObserver)
        Bus.observe(USER_COLLECT_UPDATED, lifecycleOwner, collectObserver)
        onDispose { }
    }

    BackHandler(enabled = state.resultVisible) {
        viewModel.hideResult()
    }

    SearchContent(
        state = state,
        onInputChange = viewModel::setInput,
        onClearClick = viewModel::clearInput,
        onBackClick = {
            if (state.resultVisible) {
                viewModel.hideResult()
            } else {
                navigator.goBack()
            }
        },
        onSearch = {
            keyboardController?.hide()
            viewModel.search()
        },
        onKeywordClick = {
            keyboardController?.hide()
            viewModel.setInput(it)
            viewModel.search(it)
        },
        onHistoryDelete = viewModel::deleteSearchHistory,
        onReload = viewModel::search,
        onLoadMore = viewModel::loadMore,
        onArticleClick = { navigator.openArticle(it) },
        onCollectClick = viewModel::onCollectClick,
        modifier = modifier,
    )
}

@Composable
private fun SearchContent(
    state: SearchUiState,
    onInputChange: (String) -> Unit,
    onClearClick: () -> Unit,
    onBackClick: () -> Unit,
    onSearch: () -> Unit,
    onKeywordClick: (String) -> Unit,
    onHistoryDelete: (String) -> Unit,
    onReload: () -> Unit,
    onLoadMore: () -> Unit,
    onArticleClick: (Article) -> Unit,
    onCollectClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(if (state.resultVisible) WanTheme.colors.backgroundSecondary else WanTheme.colors.backgroundPrimary),
    ) {
        SearchToolbar(
            input = state.input,
            onInputChange = onInputChange,
            onClearClick = onClearClick,
            onBackClick = onBackClick,
            onSearch = onSearch,
        )
        if (state.resultVisible) {
            SearchResultContent(
                state = state,
                onReload = onReload,
                onLoadMore = onLoadMore,
                onArticleClick = onArticleClick,
                onCollectClick = onCollectClick,
                modifier = Modifier.weight(1f),
            )
        } else {
            SearchHistoryContent(
                hotWords = state.hotWords,
                histories = state.histories,
                onKeywordClick = onKeywordClick,
                onHistoryDelete = onHistoryDelete,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun SearchToolbar(
    input: String,
    onInputChange: (String) -> Unit,
    onClearClick: () -> Unit,
    onBackClick: () -> Unit,
    onSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .shadow(2.dp)
            .background(WanTheme.colors.backgroundPrimary),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .padding(start = 4.dp, end = 4.dp)
                .size(48.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back_black_24dp),
                contentDescription = null,
                tint = WanTheme.colors.textPrimary,
                modifier = Modifier.size(24.dp),
            )
        }
        Box(
            modifier = Modifier
                .weight(1f)
                .height(32.dp)
                .shadow(1.dp, RoundedCornerShape(32.dp))
                .background(WanTheme.colors.backgroundThird, RoundedCornerShape(32.dp)),
            contentAlignment = Alignment.CenterStart,
        ) {
            BasicTextField(
                value = input,
                onValueChange = onInputChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = WanTheme.colors.textPrimary,
                    fontSize = 14.sp,
                ),
                cursorBrush = SolidColor(WanTheme.colors.textPrimary),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 24.dp, end = 32.dp),
                decorationBox = { innerTextField ->
                    if (input.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search_hint),
                            color = WanTheme.colors.textThird,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    innerTextField()
                },
            )
            if (input.isNotEmpty()) {
                Icon(
                    painter = painterResource(R.drawable.ic_close_black_24dp),
                    contentDescription = null,
                    tint = WanTheme.colors.textPrimary,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 4.dp)
                        .size(32.dp)
                        .clickable(onClick = onClearClick)
                        .padding(8.dp),
                )
            }
        }
        IconButton(
            onClick = onSearch,
            modifier = Modifier
                .padding(start = 3.dp, end = 3.dp)
                .size(48.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_search_black_24dp),
                contentDescription = null,
                tint = WanTheme.colors.textPrimary,
                modifier = Modifier.size(22.dp),
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SearchHistoryContent(
    hotWords: List<HotWord>,
    histories: List<String>,
    onKeywordClick: (String) -> Unit,
    onHistoryDelete: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WanTheme.colors.backgroundPrimary)
            .verticalScroll(rememberScrollState()),
    ) {
        if (hotWords.isNotEmpty()) {
            Text(
                text = stringResource(R.string.hot_search),
                color = WanTheme.colors.textPrimary,
                fontSize = 14.sp,
                modifier = Modifier.padding(16.dp),
            )
            FlowRow(
                modifier = Modifier.padding(start = 16.dp, top = 0.dp, end = 8.dp, bottom = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                hotWords.forEach { hotWord ->
                    HotSearchTag(
                        text = hotWord.name,
                        onClick = { onKeywordClick(hotWord.name) },
                    )
                }
            }
        }
        if (histories.isNotEmpty()) {
            Text(
                text = stringResource(R.string.search_history),
                color = WanTheme.colors.textPrimary,
                fontSize = 14.sp,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            )
            Column(
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
            ) {
                histories.forEach { words ->
                    SearchHistoryItem(
                        words = words,
                        onClick = { onKeywordClick(words) },
                        onDeleteClick = { onHistoryDelete(words) },
                    )
                }
            }
        }
    }
}

@Composable
private fun HotSearchTag(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = WanTheme.colors.textPrimary,
        fontSize = 13.sp,
        modifier = modifier
            .background(WanTheme.colors.backgroundThird, RoundedCornerShape(2.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp),
    )
}

@Composable
private fun SearchHistoryItem(
    words: String,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_access_time_black_24dp),
                contentDescription = null,
                tint = WanTheme.colors.textPrimary,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(32.dp)
                    .padding(8.dp),
            )
            Text(
                text = words,
                color = WanTheme.colors.textPrimary,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, top = 8.dp),
            )
            Icon(
                painter = painterResource(R.drawable.ic_delete_forever_black_24dp),
                contentDescription = null,
                tint = WanTheme.colors.textPrimary,
                modifier = Modifier
                    .padding(start = 16.dp, top = 8.dp)
                    .size(32.dp)
                    .clickable(onClick = onDeleteClick)
                    .padding(6.dp),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .height(0.5.dp)
                .background(WanTheme.colors.backgroundThird),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchResultContent(
    state: SearchUiState,
    onReload: () -> Unit,
    onLoadMore: () -> Unit,
    onArticleClick: (Article) -> Unit,
    onCollectClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
) {
    val listState = rememberLazyListState()
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

    when {
        state.showReload -> ReloadContent(onReload = onReload, modifier = modifier)
        state.showEmpty -> EmptySearchContent(modifier = modifier)
        else -> {
            val refreshState = rememberPullToRefreshState()
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = onReload,
                state = refreshState,
                indicator = {
                    PullToRefreshDefaults.Indicator(
                        state = refreshState,
                        isRefreshing = state.isRefreshing,
                        color = WanTheme.colors.textPrimary,
                        containerColor = WanTheme.colors.backgroundPrimary,
                        modifier = Modifier.align(Alignment.TopCenter),
                    )
                },
                modifier = modifier
                    .fillMaxSize()
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
                        )
                    }
                    if (state.loadMoreStatus.visible) {
                        item {
                            SearchLoadMoreFooter(
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

@Composable
private fun EmptySearchContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WanTheme.colors.backgroundPrimary),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_info_outline_black_24dp),
            contentDescription = null,
            tint = WanTheme.colors.textThird,
            modifier = Modifier.size(48.dp),
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.no_data),
            color = WanTheme.colors.textThird,
            fontSize = 14.sp,
        )
    }
}

@Composable
private fun SearchLoadMoreFooter(
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
                        text = stringResource(R.string.loading),
                        color = WanTheme.colors.textPrimary,
                        fontSize = 12.sp,
                    )
                }
            }
            HomeLoadMoreStatus.Error -> {
                Text(
                    text = stringResource(R.string.load_failed),
                    color = WanTheme.colors.textPrimary,
                    fontSize = 14.sp,
                )
            }
            HomeLoadMoreStatus.End -> {
                Text(
                    text = stringResource(R.string.load_end),
                    color = WanTheme.colors.textThird,
                    fontSize = 14.sp,
                )
            }
            HomeLoadMoreStatus.Complete -> Unit
        }
    }
}

private fun WanNavigator.openArticle(article: Article) {
    navigate(article.toArticleDetailRoute())
}

@Preview(showBackground = true)
@Composable
private fun SearchScreenPreview() {
    WanAndroidTheme {
        SearchContent(
            state = SearchUiState(
                input = "Compose",
                hotWords = listOf(
                    HotWord(id = 1, link = "", order = 0, name = "Android", visible = 1),
                    HotWord(id = 2, link = "", order = 1, name = "Kotlin", visible = 1),
                ),
                histories = listOf("Jetpack Compose", "Nav3"),
            ),
            onInputChange = {},
            onClearClick = {},
            onBackClick = {},
            onSearch = {},
            onKeywordClick = {},
            onHistoryDelete = {},
            onReload = {},
            onLoadMore = {},
            onArticleClick = {},
            onCollectClick = {},
        )
    }
}
