package com.xiaojianjun.wanandroid.ui.compose.navigationpage

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.bean.Navigation
import com.xiaojianjun.wanandroid.ui.compose.components.ReloadContent
import com.xiaojianjun.wanandroid.ui.compose.home.HomeChromeState
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanNavigator
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute
import com.xiaojianjun.wanandroid.ui.compose.navigation.toArticleDetailRoute
import com.xiaojianjun.wanandroid.ui.compose.theme.WanAndroidTheme
import com.xiaojianjun.wanandroid.ui.compose.theme.WanTheme
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationScreen(
    navigator: WanNavigator,
    chromeState: HomeChromeState,
    reselectVersion: Int,
    modifier: Modifier = Modifier,
    viewModel: NavigationComposeViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val refreshState = rememberPullToRefreshState()
    val listState = rememberLazyListState()
    var scrollOffset by rememberSaveable { mutableFloatStateOf(0f) }
    LaunchedEffect(reselectVersion) {
        if (reselectVersion > 0) {
            listState.animateScrollToItem(0)
            scrollOffset = 0f
            chromeState.onToolbarOffsetChanged(0)
        }
    }
    val nestedScrollConnection = remember(chromeState) {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (available.y == 0f) return Offset.Zero
                scrollOffset += available.y
                chromeState.onToolbarOffsetChanged(scrollOffset.roundToInt())
                return Offset.Zero
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WanTheme.colors.backgroundPrimary),
    ) {
        NavigationToolbar()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .clipToBounds()
                .nestedScroll(nestedScrollConnection),
        ) {
            PullToRefreshBox(
                isRefreshing = state.isRefreshing,
                onRefresh = viewModel::refresh,
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
                modifier = Modifier.fillMaxSize(),
            ) {
                NavigationContent(
                    navigations = state.navigations,
                    listState = listState,
                    onArticleClick = { article -> navigator.openArticle(article) },
                )
            }

            FloatingNavigationTitle(
                navigations = state.navigations,
                listState = listState,
                modifier = Modifier.align(Alignment.TopCenter),
            )

            if (state.showReload) {
                ReloadContent(
                    onReload = viewModel::refresh,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(WanTheme.colors.backgroundPrimary),
                )
            }
        }
    }
}

@Composable
private fun NavigationToolbar(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(WanTheme.colors.backgroundPrimary),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(R.string.navigation),
            color = WanTheme.colors.textPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
        )
        ToolbarBottomShadow(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun NavigationContent(
    navigations: List<Navigation>,
    listState: LazyListState,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(WanTheme.colors.backgroundSecondary),
    ) {
        itemsIndexed(
            items = navigations,
            key = { _, navigation -> navigation.cid },
        ) { _, navigation ->
            NavigationGroup(
                navigation = navigation,
                onArticleClick = onArticleClick,
            )
        }
        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NavigationGroup(
    navigation: Navigation,
    onArticleClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .background(WanTheme.colors.backgroundPrimary)
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 8.dp),
    ) {
        Text(
            text = navigation.name,
            color = WanTheme.colors.textSecondary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth(),
        )
        FlowRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            navigation.articles.forEach { article ->
                NavigationTag(
                    article = article,
                    onClick = { onArticleClick(article) },
                )
            }
        }
    }
}

@Composable
private fun NavigationTag(
    article: Article,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = article.title.orEmpty(),
        color = WanTheme.colors.textPrimary,
        fontSize = 13.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = modifier
            .clip(RoundedCornerShape(24.dp))
            .background(WanTheme.colors.backgroundThird)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 4.dp),
    )
}

@Composable
private fun FloatingNavigationTitle(
    navigations: List<Navigation>,
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    if (navigations.isEmpty()) return

    val density = LocalDensity.current
    var titleHeightPx by remember { mutableIntStateOf(with(density) { 50.dp.roundToPx() }) }
    val firstVisibleIndex by remember {
        derivedStateOf { listState.firstVisibleItemIndex.coerceAtMost(navigations.lastIndex) }
    }
    val nextItemOffset by remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo
                .firstOrNull { it.index == firstVisibleIndex + 1 }
                ?.offset
        }
    }
    val offsetY = nextItemOffset
        ?.takeIf { it < titleHeightPx }
        ?.minus(titleHeightPx)
        ?: 0

    Text(
        text = navigations[firstVisibleIndex].name,
        color = WanTheme.colors.textSecondary,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier
            .fillMaxWidth()
            .offset { IntOffset(0, offsetY) }
            .onSizeChanged { titleHeightPx = it.height }
            .background(WanTheme.colors.backgroundPrimary)
            .padding(start = 16.dp, top = 16.dp, bottom = 16.dp),
    )
}

@Composable
private fun ToolbarBottomShadow(
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(2.dp),
    ) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Black.copy(alpha = 0.05f),
                    Color.Transparent,
                ),
            ),
        )
    }
}

private fun WanNavigator.openArticle(article: Article) {
    navigate(article.toArticleDetailRoute())
}

@Preview(showBackground = true)
@Composable
private fun NavigationGroupPreview() {
    WanAndroidTheme {
        NavigationGroup(
            navigation = Navigation(
                cid = 1,
                name = "移动开发",
                articles = mutableListOf(
                    Article(id = 1, title = "Android"),
                    Article(id = 2, title = "Kotlin"),
                    Article(id = 3, title = "Jetpack Compose"),
                    Article(id = 4, title = "Flutter"),
                ),
            ),
            onArticleClick = {},
        )
    }
}
