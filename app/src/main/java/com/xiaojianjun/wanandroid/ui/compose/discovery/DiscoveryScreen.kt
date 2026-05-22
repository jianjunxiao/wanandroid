package com.xiaojianjun.wanandroid.ui.compose.discovery

import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.common.core.normalizeWanAndroidImageUrl
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.bean.Banner
import com.xiaojianjun.wanandroid.model.bean.Frequently
import com.xiaojianjun.wanandroid.model.bean.HotWord
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.ui.compose.components.ReloadContent
import com.xiaojianjun.wanandroid.ui.compose.home.HomeChromeState
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanNavigator
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute
import com.xiaojianjun.wanandroid.ui.compose.navigation.toArticleDetailRoute
import com.xiaojianjun.wanandroid.ui.compose.theme.WanTheme
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoveryScreen(
    navigator: WanNavigator,
    chromeState: HomeChromeState,
    modifier: Modifier = Modifier,
    viewModel: DiscoveryComposeViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val refreshState = rememberPullToRefreshState()
    val listState = rememberLazyListState()
    var scrollOffset by remember { mutableFloatStateOf(0f) }
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
        DiscoveryToolbar(
            onAddClick = {
                if (isLogin()) {
                    navigator.navigate(WanRoute.ShareArticle)
                } else {
                    navigator.navigate(WanRoute.Login)
                }
            },
            onSearchClick = { navigator.navigate(WanRoute.Search()) },
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
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
                DiscoveryContent(
                    state = state,
                    listState = listState,
                    onBannerClick = { banner ->
                        navigator.openArticle(
                            Article(title = banner.title, link = banner.url),
                        )
                    },
                    onHotWordClick = { hotWord ->
                        navigator.navigate(WanRoute.Search(initialKeywords = hotWord.name))
                    },
                    onFrequentlyClick = { frequently ->
                        navigator.openArticle(
                            Article(title = frequently.name, link = frequently.link),
                        )
                    },
                )
            }

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
private fun DiscoveryToolbar(
    onAddClick: () -> Unit,
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(WanTheme.colors.backgroundPrimary),
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 5.dp)
                .size(48.dp)
                .clickable(onClick = onAddClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_add_black_24dp),
                contentDescription = null,
                tint = WanTheme.colors.textPrimary,
                modifier = Modifier.size(26.dp),
            )
        }
        Text(
            text = stringResource(R.string.discovery),
            color = WanTheme.colors.textPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Center),
        )
        Box(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 3.dp)
                .size(48.dp)
                .clickable(onClick = onSearchClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_search_black_24dp),
                contentDescription = null,
                tint = WanTheme.colors.textPrimary,
                modifier = Modifier.size(22.dp),
            )
        }
        ToolbarBottomShadow(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun DiscoveryContent(
    state: DiscoveryUiState,
    listState: androidx.compose.foundation.lazy.LazyListState,
    onBannerClick: (Banner) -> Unit,
    onHotWordClick: (HotWord) -> Unit,
    onFrequentlyClick: (Frequently) -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        state = listState,
        modifier = modifier
            .fillMaxSize()
            .background(WanTheme.colors.backgroundPrimary),
    ) {
        item {
            BannerSection(
                banners = state.banners,
                onBannerClick = onBannerClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp),
            )
        }
        if (state.hotWords.isNotEmpty()) {
            item {
                SectionTitle(
                    text = stringResource(R.string.everyone_search),
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                )
            }
            items(ceilDiv(state.hotWords.size, 3)) { row ->
                HotWordRow(
                    hotWords = state.hotWords.drop(row * 3).take(3),
                    onClick = onHotWordClick,
                    modifier = Modifier.padding(start = 16.dp, top = if (row == 0) 16.dp else 0.dp, end = 8.dp),
                )
            }
        }
        if (state.frequentlyList.isNotEmpty()) {
            item {
                SectionTitle(
                    text = stringResource(R.string.frequently_website),
                    modifier = Modifier.padding(start = 16.dp, top = 8.dp, end = 16.dp),
                )
                FrequentlyTags(
                    frequentlyList = state.frequentlyList,
                    onClick = onFrequentlyClick,
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 8.dp, bottom = 8.dp),
                )
            }
        }
        item {
            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Composable
private fun BannerSection(
    banners: List<Banner>,
    onBannerClick: (Banner) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(16f / 7f)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        WanTheme.colors.backgroundPrimary,
                        WanTheme.colors.textPrimary,
                    ),
                ),
            ),
    ) {
        if (banners.isNotEmpty()) {
            val pagerState = rememberPagerState(pageCount = { banners.size })
            LaunchedEffect(banners, pagerState) {
                while (banners.size > 1) {
                    delay(5_000)
                    val nextPage = (pagerState.currentPage + 1) % banners.size
                    pagerState.animateScrollToPage(
                        page = nextPage,
                        animationSpec = tween(durationMillis = 1_500),
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
            ) { page ->
                AsyncImage(
                    model = banners[page].imagePath.normalizeWanAndroidImageUrl(),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable { onBannerClick(banners[page]) },
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = WanTheme.colors.textSecondary,
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier.fillMaxWidth(),
    )
}

@Composable
private fun HotWordRow(
    hotWords: List<HotWord>,
    onClick: (HotWord) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier.fillMaxWidth()) {
        hotWords.forEach { hotWord ->
            Text(
                text = hotWord.name,
                color = WanTheme.colors.textPrimary,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp, bottom = 8.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(WanTheme.colors.backgroundSecondary)
                    .clickable { onClick(hotWord) }
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
        }
        repeat(3 - hotWords.size) {
            Spacer(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FrequentlyTags(
    frequentlyList: List<Frequently>,
    onClick: (Frequently) -> Unit,
    modifier: Modifier = Modifier,
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        frequentlyList.forEach { frequently ->
            Text(
                text = frequently.name,
                color = WanTheme.colors.textPrimary,
                fontSize = 13.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(WanTheme.colors.backgroundSecondary)
                    .clickable { onClick(frequently) }
                    .padding(horizontal = 12.dp, vertical = 4.dp),
            )
        }
    }
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

private fun ceilDiv(value: Int, divisor: Int): Int = (value + divisor - 1) / divisor

private fun WanNavigator.openArticle(article: Article) {
    navigate(article.toArticleDetailRoute())
}
