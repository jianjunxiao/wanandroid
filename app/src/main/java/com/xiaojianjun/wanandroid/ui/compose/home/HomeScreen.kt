package com.xiaojianjun.wanandroid.ui.compose.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.bean.Category
import com.xiaojianjun.wanandroid.ui.compose.components.ArticleCard
import com.xiaojianjun.wanandroid.ui.compose.components.LoadingContent
import com.xiaojianjun.wanandroid.ui.compose.components.ReloadContent
import com.xiaojianjun.wanandroid.ui.compose.components.SimpleArticleCard
import com.xiaojianjun.wanandroid.ui.compose.common.htmlPlainText
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanNavigator
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute
import com.xiaojianjun.wanandroid.ui.compose.navigation.toArticleDetailRoute
import com.xiaojianjun.wanandroid.ui.compose.theme.WanTheme
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

private enum class HomeTab(val titleRes: Int) {
    Popular(R.string.popular_articles),
    Latest(R.string.latest_project),
    Plaza(R.string.plaza),
    Project(R.string.project_category),
    Wechat(R.string.wechat_public),
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigator: WanNavigator,
    chromeState: HomeChromeState,
    reselectVersion: Int,
    modifier: Modifier = Modifier,
    popularViewModel: PopularComposeViewModel = viewModel(),
) {
    val density = LocalDensity.current
    val toolbarHeightPx = with(density) { 48.dp.roundToPx() }
    val appBarHeightPx = with(density) { 90.dp.roundToPx() }
    var toolbarOffset by rememberSaveable { mutableIntStateOf(0) }
    val contentTopPadding = with(density) { (appBarHeightPx + toolbarOffset).toDp() }
    val pagerState = rememberPagerState(pageCount = { HomeTab.entries.size })
    val scope = rememberCoroutineScope()
    val popularListState = rememberLazyListState()
    val latestListState = rememberLazyListState()
    val plazaListState = rememberLazyListState()
    val projectListState = rememberLazyListState()
    val wechatListState = rememberLazyListState()

    LaunchedEffect(reselectVersion) {
        if (reselectVersion > 0) {
            when (HomeTab.entries[pagerState.currentPage]) {
                HomeTab.Popular -> popularListState.animateScrollToItem(0)
                HomeTab.Latest -> latestListState.animateScrollToItem(0)
                HomeTab.Plaza -> plazaListState.animateScrollToItem(0)
                HomeTab.Project -> projectListState.animateScrollToItem(0)
                HomeTab.Wechat -> wechatListState.animateScrollToItem(0)
            }
            toolbarOffset = 0
            chromeState.onToolbarOffsetChanged(0)
        }
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                if (delta == 0f) return Offset.Zero
                val oldOffset = toolbarOffset
                val newOffset = (oldOffset + delta.roundToInt()).coerceIn(-toolbarHeightPx, 0)
                if (newOffset != oldOffset) {
                    toolbarOffset = newOffset
                    chromeState.onToolbarOffsetChanged(newOffset)
                }
                return Offset(x = 0f, y = (newOffset - oldOffset).toFloat())
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WanTheme.colors.backgroundSecondary)
            .nestedScroll(nestedScrollConnection),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = contentTopPadding),
        ) {
            HorizontalPager(
                state = pagerState,
                flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                when (HomeTab.entries[page]) {
                    HomeTab.Popular -> PopularRoute(
                        viewModel = popularViewModel,
                        navigator = navigator,
                        listState = popularListState,
                    )
                    HomeTab.Latest -> HomeArticleRoute(
                        viewModel = viewModel<LatestComposeViewModel>(),
                        navigator = navigator,
                        listState = latestListState,
                    )
                    HomeTab.Plaza -> HomeArticleRoute(
                        viewModel = viewModel<PlazaComposeViewModel>(),
                        navigator = navigator,
                        listState = plazaListState,
                    )
                    HomeTab.Project -> HomeArticleRoute(
                        viewModel = viewModel<ProjectComposeViewModel>(),
                        navigator = navigator,
                        listState = projectListState,
                    )
                    HomeTab.Wechat -> HomeArticleRoute(
                        viewModel = viewModel<WechatComposeViewModel>(),
                        navigator = navigator,
                        listState = wechatListState,
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(0, toolbarOffset) },
        ) {
            HomeSearchToolbar(onClick = { navigator.navigate(WanRoute.Search()) })
            HomeTabRow(
                selectedIndex = pagerState.currentPage,
                selectionOffset = pagerState.currentPageOffsetFraction,
                onTabClick = { index -> scope.launch { pagerState.animateScrollToPage(index) } },
            )
            AppBarBottomShadow()
        }
    }
}

@Composable
private fun HomeSearchToolbar(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(WanTheme.colors.backgroundPrimary),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
                .background(WanTheme.colors.backgroundThird, RoundedCornerShape(32.dp))
                .clickable(onClick = onClick),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_search_black_24dp),
                contentDescription = null,
                tint = WanTheme.colors.textThird,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .size(18.dp),
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(R.string.search_hint),
                color = WanTheme.colors.textThird,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun AppBarBottomShadow(
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(4.dp),
    ) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Black.copy(alpha = 0.08f),
                    Color.Black.copy(alpha = 0.03f),
                    Color.Transparent,
                ),
            ),
        )
    }
}

@Composable
private fun HomeTabRow(
    selectedIndex: Int,
    selectionOffset: Float,
    onTabClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabCount = HomeTab.entries.size
    val selectedPosition = (selectedIndex + selectionOffset)
        .coerceIn(0f, (tabCount - 1).toFloat())
    val selectionProgress = homeTabSelectionProgress(
        tabCount = tabCount,
        currentPage = selectedIndex,
        currentPageOffsetFraction = selectionOffset,
    )
    val indicatorColor = WanTheme.colors.textPrimary

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp)
            .background(WanTheme.colors.backgroundPrimary),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            HomeTab.entries.forEachIndexed { index, tab ->
                val selected = selectedIndex == index
                val progress = selectionProgress[index]
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .clickable { onTabClick(index) },
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(tab.titleRes),
                        color = lerp(
                            start = WanTheme.colors.textThird,
                            stop = WanTheme.colors.textPrimary,
                            fraction = progress,
                        ),
                        fontSize = 14.sp,
                        fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
        Canvas(modifier = Modifier.fillMaxSize()) {
            val tabWidth = size.width / tabCount
            val indicatorWidth = tabWidth * 0.5f
            val indicatorHeight = 2.dp.toPx()
            val left = tabWidth * selectedPosition + (tabWidth - indicatorWidth) / 2f
            drawRect(
                color = indicatorColor,
                topLeft = Offset(left, size.height - indicatorHeight),
                size = androidx.compose.ui.geometry.Size(indicatorWidth, indicatorHeight),
            )
        }
    }
}

@Composable
private fun PopularRoute(
    viewModel: PopularComposeViewModel,
    navigator: WanNavigator,
    listState: LazyListState,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, navigator) {
        viewModel.popularEvents.collect { event ->
            when (event) {
                is PopularEvent.OpenLoginForCollect -> navigator.navigate(WanRoute.Login)
            }
        }
    }

    PopularContent(
        state = state,
        onReload = viewModel::refresh,
        onLoadMore = viewModel::loadMore,
        onArticleClick = { article -> navigator.openArticle(article) },
        onCollectClick = viewModel::onCollectClick,
        listState = listState,
    )
}

@Composable
private fun HomeArticleRoute(
    viewModel: BaseHomeArticleViewModel,
    navigator: WanNavigator,
    listState: LazyListState,
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, navigator) {
        viewModel.articleEvents.collectLoginNavigation(navigator)
    }

    HomeArticleContent(
        state = state,
        onReload = viewModel::refresh,
        onListReload = viewModel::refresh,
        onLoadMore = viewModel::loadMore,
        onCategoryClick = viewModel::selectCategory,
        onArticleClick = { article -> navigator.openArticle(article) },
        onCollectClick = viewModel::onCollectClick,
        listState = listState,
    )
}

private suspend fun SharedFlow<HomeArticleEvent>.collectLoginNavigation(navigator: WanNavigator) {
    collect { event ->
        when (event) {
            is HomeArticleEvent.OpenLoginForCollect -> navigator.navigate(WanRoute.Login)
        }
    }
}

@Composable
fun PopularContent(
    state: PopularUiState,
    onReload: () -> Unit,
    onLoadMore: () -> Unit,
    onArticleClick: (Article) -> Unit,
    onCollectClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    HomeArticleContent(
        state = HomeArticleUiState(
            articles = state.articles,
            isRefreshing = state.isRefreshing,
            isLoadingMore = state.isLoadingMore,
            showReload = state.showReload,
            loadMoreStatus = state.loadMoreStatus,
        ),
        onReload = onReload,
        onListReload = onReload,
        onLoadMore = onLoadMore,
        onCategoryClick = {},
        onArticleClick = onArticleClick,
        onCollectClick = onCollectClick,
        listState = listState,
        modifier = modifier,
    )
}

@Composable
private fun HomeArticleContent(
    state: HomeArticleUiState,
    onReload: () -> Unit,
    onListReload: () -> Unit,
    onLoadMore: () -> Unit,
    onCategoryClick: (Int) -> Unit,
    onArticleClick: (Article) -> Unit,
    onCollectClick: (Article) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    when {
        state.showReload -> ReloadContent(onReload = onReload, modifier = modifier)
        state.shouldShowFullPageLoading() -> LoadingContent(modifier = modifier)
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
            val pullToRefreshState = rememberPullToRefreshState()

            Column(
                modifier = modifier
                    .fillMaxSize()
                    .background(WanTheme.colors.backgroundSecondary),
            ) {
                if (state.categories.isNotEmpty()) {
                    CategoryRow(
                        categories = state.categories,
                        checkedCategory = state.checkedCategory,
                        onCategoryClick = onCategoryClick,
                    )
                }
                Box(modifier = Modifier.fillMaxSize()) {
                    if (state.showListReload) {
                        ReloadContent(onReload = onListReload)
                    } else {
                        PullToRefreshBox(
                            isRefreshing = state.isRefreshing,
                            onRefresh = onReload,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(WanTheme.colors.backgroundSecondary),
                            state = pullToRefreshState,
                            indicator = {
                                PullToRefreshDefaults.Indicator(
                                    modifier = Modifier.align(Alignment.TopCenter),
                                    isRefreshing = state.isRefreshing,
                                    state = pullToRefreshState,
                                    containerColor = WanTheme.colors.backgroundPrimary,
                                    color = WanTheme.colors.textPrimary,
                                )
                            },
                        ) {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(WanTheme.colors.backgroundSecondary),
                                state = listState,
                            ) {
                                items(
                                    items = state.articles,
                                    key = { it.id },
                                ) { article ->
                                    if (state.useSimpleItem) {
                                        SimpleArticleCard(
                                            article = article,
                                            onClick = { onArticleClick(article) },
                                            onCollectClick = { onCollectClick(article) },
                                        )
                                    } else {
                                        ArticleCard(
                                            article = article,
                                            onClick = { onArticleClick(article) },
                                            onCollectClick = { onCollectClick(article) },
                                        )
                                    }
                                }
                                if (state.loadMoreStatus.visible) {
                                    item {
                                        LegacyLoadMoreFooter(
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
    }
}

@Composable
private fun LegacyLoadMoreFooter(
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

@Composable
private fun CategoryRow(
    categories: List<Category>,
    checkedCategory: Int,
    onCategoryClick: (Int) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(1f),
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .background(WanTheme.colors.backgroundPrimary)
                .padding(top = 10.dp),
        ) {
            items(categories.size) { index ->
                val selected = index == checkedCategory
                val categoryShape = RoundedCornerShape(24.dp)
                Text(
                    text = categories[index].name.htmlPlainText(),
                    color = if (selected) WanTheme.colors.textPrimary else WanTheme.colors.textThird,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .padding(start = if (index == 0) 8.dp else 0.dp, end = 8.dp, bottom = 10.dp)
                        .clip(categoryShape)
                        .background(
                            color = if (selected) WanTheme.colors.backgroundThird else WanTheme.colors.backgroundSecondary,
                            shape = categoryShape,
                        )
                        .border(BorderStroke(0.dp, WanTheme.colors.backgroundSecondary), categoryShape)
                        .clickable { onCategoryClick(index) }
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                )
            }
        }
        CategoryBottomShadow(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = LegacyCategoryRowStyle.bottomShadowDrawHeight),
        )
    }
}

@Composable
private fun CategoryBottomShadow(
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(LegacyCategoryRowStyle.bottomShadowDrawHeight),
    ) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Black.copy(alpha = 0.05f),
                    Color.Black.copy(alpha = 0.02f),
                    Color.Transparent,
                ),
            ),
        )
    }
}

private fun WanNavigator.openArticle(article: Article) {
    navigate(article.toArticleDetailRoute())
}
