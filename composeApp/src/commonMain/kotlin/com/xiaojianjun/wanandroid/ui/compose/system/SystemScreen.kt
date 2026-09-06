package com.xiaojianjun.wanandroid.ui.compose.system

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.bean.Category
import com.xiaojianjun.wanandroid.resources.*
import com.xiaojianjun.wanandroid.ui.compose.common.htmlPlainText
import com.xiaojianjun.wanandroid.ui.compose.components.LoadingContent
import com.xiaojianjun.wanandroid.ui.compose.components.ReloadContent
import com.xiaojianjun.wanandroid.ui.compose.components.SimpleArticleCard
import com.xiaojianjun.wanandroid.ui.compose.home.HomeChromeState
import com.xiaojianjun.wanandroid.ui.compose.home.HomeLoadMoreStatus
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanNavigator
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute
import com.xiaojianjun.wanandroid.ui.compose.navigation.toArticleDetailRoute
import com.xiaojianjun.wanandroid.ui.compose.theme.WanTheme
import kotlin.math.roundToInt
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemScreen(
    navigator: WanNavigator,
    chromeState: HomeChromeState,
    modifier: Modifier = Modifier,
    viewModel: SystemComposeViewModel = viewModel { SystemComposeViewModel() },
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val density = LocalDensity.current
    val showCategoryTabs = state.shouldShowCategoryTabs()
    val toolbarHeightPx = with(density) { 48.dp.roundToPx() }
    val appBarHeightPx = with(density) { if (showCategoryTabs) 90.dp.roundToPx() else 48.dp.roundToPx() }
    var toolbarOffset by rememberSaveable { mutableIntStateOf(0) }
    val contentTopPadding = with(density) { (appBarHeightPx + toolbarOffset).toDp() }
    val listState = rememberLazyListState()
    val parentTabState = rememberLazyListState()
    val selectionScrollTracker = remember { SystemSelectionScrollTracker() }
    var showCategorySheet by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel, navigator) {
        viewModel.systemEvents.collect { event ->
            when (event) {
                SystemEvent.OpenLoginForCollect -> navigator.navigate(WanRoute.Login)
            }
        }
    }

    LaunchedEffect(state.selectedParent, state.selectedChild) {
        if (selectionScrollTracker.shouldScrollToTop(state.selectedParent, state.selectedChild)) {
            listState.scrollToItem(0)
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
            SystemContent(
                state = state,
                listState = listState,
                onReload = viewModel::refreshCategories,
                onListReload = { viewModel.refreshArticles(clearList = true) },
                onRefresh = viewModel::refreshArticles,
                onLoadMore = viewModel::loadMore,
                onParentSelected = viewModel::selectParent,
                onChildSelected = viewModel::selectChild,
                onArticleClick = { article -> navigator.openArticle(article) },
                onCollectClick = viewModel::onCollectClick,
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset { IntOffset(0, toolbarOffset) },
        ) {
            SystemToolbar(
                filterVisible = showCategoryTabs,
                onFilterClick = { showCategorySheet = true },
            )
            if (showCategoryTabs) {
                SystemParentTabRow(
                    categories = state.categories,
                    selectedParent = state.selectedParent,
                    onParentClick = viewModel::selectParent,
                    listState = parentTabState,
                )
            }
            AppBarBottomShadow()
        }
    }

    if (showCategorySheet) {
        SystemCategorySheet(
            state = state,
            sheetHeightOffset = 48.dp,
            onDismiss = { showCategorySheet = false },
            onCategoryClick = { parent, child ->
                showCategorySheet = false
                viewModel.selectCategory(parent, child)
            },
        )
    }
}

@Composable
private fun SystemContent(
    state: SystemUiState,
    listState: LazyListState,
    onReload: () -> Unit,
    onListReload: () -> Unit,
    onRefresh: () -> Unit,
    onLoadMore: () -> Unit,
    onParentSelected: (Int) -> Unit,
    onChildSelected: (Int) -> Unit,
    onArticleClick: (Article) -> Unit,
    onCollectClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
) {
    when {
        state.showReload -> ReloadContent(onReload = onReload, modifier = modifier)
        state.isLoading -> LoadingContent(modifier = modifier)
        state.categories.isEmpty() -> Box(modifier = modifier.fillMaxSize())
        else -> {
            val pagerState = rememberPagerState(pageCount = { state.categories.size })
            val scope = rememberCoroutineScope()

            LaunchedEffect(pagerState.currentPage) {
                onParentSelected(pagerState.currentPage)
            }
            LaunchedEffect(state.selectedParent) {
                if (pagerState.currentPage != state.selectedParent) {
                    pagerState.scrollToPage(state.selectedParent)
                }
            }

            HorizontalPager(
                state = pagerState,
                flingBehavior = PagerDefaults.flingBehavior(state = pagerState),
                modifier = modifier.fillMaxSize(),
            ) { page ->
                SystemPagerContent(
                    state = state.copy(selectedParent = page),
                    listState = listState,
                    onRefresh = onRefresh,
                    onListReload = onListReload,
                    onLoadMore = onLoadMore,
                    onChildSelected = { index ->
                        scope.launch {
                            if (pagerState.currentPage != page) {
                                pagerState.animateScrollToPage(page)
                            }
                            onChildSelected(index)
                        }
                    },
                    onArticleClick = onArticleClick,
                    onCollectClick = onCollectClick,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SystemPagerContent(
    state: SystemUiState,
    listState: LazyListState,
    onRefresh: () -> Unit,
    onListReload: () -> Unit,
    onLoadMore: () -> Unit,
    onChildSelected: (Int) -> Unit,
    onArticleClick: (Article) -> Unit,
    onCollectClick: (Article) -> Unit,
) {
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(WanTheme.colors.backgroundSecondary),
    ) {
        SystemChildCategoryRow(
            categories = state.children,
            selectedChild = state.selectedChild,
            onCategoryClick = onChildSelected,
        )
        Box(modifier = Modifier.fillMaxSize()) {
            if (state.showListReload) {
                ReloadContent(onReload = onListReload)
            } else {
                val pullToRefreshState = rememberPullToRefreshState()
                PullToRefreshBox(
                    isRefreshing = state.isRefreshing,
                    onRefresh = onRefresh,
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
                            SimpleArticleCard(
                                article = article,
                                onClick = { onArticleClick(article) },
                                onCollectClick = { onCollectClick(article) },
                            )
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

@Composable
private fun SystemToolbar(
    filterVisible: Boolean,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(WanTheme.colors.backgroundPrimary),
    ) {
        Text(
            text = stringResource(Res.string.system),
            color = WanTheme.colors.textPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Center),
        )
        if (filterVisible) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(48.dp)
                    .clickable(onClick = onFilterClick),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_filter),
                    contentDescription = null,
                    tint = WanTheme.colors.textPrimary,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

@Composable
private fun SystemParentTabRow(
    categories: List<Category>,
    selectedParent: Int,
    onParentClick: (Int) -> Unit,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(selectedParent, categories.size) {
        if (selectedParent in categories.indices) {
            listState.animateScrollToItem(selectedParent)
        }
    }

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp)
            .background(WanTheme.colors.backgroundPrimary),
        state = listState,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        items(categories.size) { index ->
            val selected = selectedParent == index
            Box(
                modifier = Modifier
                    .height(42.dp)
                    .clickable { onParentClick(index) }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = categories[index].name.htmlPlainText(),
                    color = if (selected) WanTheme.colors.textPrimary else WanTheme.colors.textThird,
                    fontSize = 14.sp,
                    fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                if (selected) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(WanTheme.colors.textPrimary),
                    )
                }
            }
        }
    }
}

@Composable
private fun SystemChildCategoryRow(
    categories: List<Category>,
    selectedChild: Int,
    onCategoryClick: (Int) -> Unit,
) {
    val listState = rememberLazyListState()

    LaunchedEffect(selectedChild, categories.size) {
        if (selectedChild in categories.indices) {
            listState.animateScrollToItem(selectedChild)
        }
    }

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
            state = listState,
        ) {
            items(categories.size) { index ->
                CategoryChip(
                    text = categories[index].name.htmlPlainText(),
                    selected = selectedChild == index,
                    modifier = Modifier.padding(
                        start = if (index == 0) 8.dp else 0.dp,
                        end = 8.dp,
                        bottom = 10.dp,
                    ),
                    onClick = { onCategoryClick(index) },
                )
            }
        }
        CategoryBottomShadow(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = 4.dp),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun SystemCategorySheet(
    state: SystemUiState,
    sheetHeightOffset: Dp,
    onDismiss: () -> Unit,
    onCategoryClick: (Int, Int) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val listState = rememberLazyListState()

    LaunchedEffect(state.selectedParent, state.categories.size) {
        if (state.selectedParent in state.categories.indices) {
            listState.scrollToItem(state.selectedParent)
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier
            .fillMaxHeight()
            .padding(top = sheetHeightOffset),
        sheetState = sheetState,
        containerColor = WanTheme.colors.backgroundPrimary,
        contentColor = WanTheme.colors.textPrimary,
        scrimColor = Color.Black.copy(alpha = 0.32f),
        dragHandle = null,
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(WanTheme.colors.backgroundPrimary),
            state = listState,
        ) {
            items(state.categories.size) { parentIndex ->
                val parent = state.categories[parentIndex]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 16.dp, end = 16.dp),
                ) {
                    Text(
                        text = parent.name.htmlPlainText(),
                        color = WanTheme.colors.textPrimary,
                        fontSize = 14.sp,
                    )
                    FlowRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        parent.children.forEachIndexed { childIndex, child ->
                            CategoryChip(
                                text = child.name.htmlPlainText(),
                                selected = state.selectedParent == parentIndex && state.selectedChild == childIndex,
                                modifier = Modifier.padding(bottom = 8.dp),
                                onClick = { onCategoryClick(parentIndex, childIndex) },
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .height(1.dp)
                            .background(WanTheme.colors.backgroundSecondary),
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(24.dp)
    Text(
        text = text,
        color = if (selected) WanTheme.colors.textPrimary else WanTheme.colors.textThird,
        fontSize = 13.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .clip(shape)
            .background(
                color = if (selected) WanTheme.colors.backgroundThird else WanTheme.colors.backgroundSecondary,
                shape = shape,
            )
            .border(BorderStroke(0.dp, WanTheme.colors.backgroundSecondary), shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
    )
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
private fun CategoryBottomShadow(
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
                    Color.Black.copy(alpha = 0.05f),
                    Color.Black.copy(alpha = 0.02f),
                    Color.Transparent,
                ),
            ),
        )
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

private fun WanNavigator.openArticle(article: Article) {
    navigate(article.toArticleDetailRoute())
}
