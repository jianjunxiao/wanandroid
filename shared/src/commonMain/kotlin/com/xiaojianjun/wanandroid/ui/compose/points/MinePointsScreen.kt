package com.xiaojianjun.wanandroid.ui.compose.points

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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshDefaults
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xiaojianjun.wanandroid.di.appViewModel
import com.xiaojianjun.wanandroid.ext.toDateTime
import com.xiaojianjun.wanandroid.model.bean.PointRecord
import com.xiaojianjun.wanandroid.resources.*
import com.xiaojianjun.wanandroid.ui.compose.components.ReloadContent
import com.xiaojianjun.wanandroid.ui.compose.components.ToolbarBottomShadow
import com.xiaojianjun.wanandroid.ui.compose.home.HomeLoadMoreStatus
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanNavigator
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute
import com.xiaojianjun.wanandroid.ui.compose.theme.WanTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MinePointsScreen(
    navigator: WanNavigator,
    modifier: Modifier = Modifier,
    viewModel: MinePointsComposeViewModel = appViewModel { MinePointsComposeViewModel(minePointsRepository) },
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WanTheme.colors.backgroundSecondary),
    ) {
        MinePointsToolbar(
            onBackClick = { navigator.goBack() },
            onRankClick = { navigator.navigate(WanRoute.PointsRank) },
        )
        MinePointsContent(
            state = state,
            onReload = viewModel::refresh,
            onLoadMore = viewModel::loadMore,
            listState = listState,
            modifier = Modifier.weight(1f),
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MinePointsContent(
    state: MinePointsUiState,
    onReload: () -> Unit,
    onLoadMore: () -> Unit,
    listState: androidx.compose.foundation.lazy.LazyListState,
    modifier: Modifier = Modifier,
) {
    when {
        state.showReload -> ReloadContent(onReload = onReload, modifier = modifier)
        else -> {
            val shouldLoadMore by remember(state.records.size, state.loadMoreStatus) {
                derivedStateOf {
                    val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                    state.loadMoreStatus == HomeLoadMoreStatus.Complete &&
                        lastVisible != null &&
                        lastVisible >= state.records.lastIndex - 2
                }
            }
            LaunchedEffect(shouldLoadMore) {
                if (shouldLoadMore) onLoadMore()
            }

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
                    state.totalPoints?.let { totalPoints ->
                        item {
                            MinePointsHeader(
                                points = totalPoints.coinCount,
                                levelRank = stringResource(Res.string.level_rank, totalPoints.level, totalPoints.rank),
                            )
                        }
                    }
                    items(
                        count = state.records.size,
                        key = { state.records[it].id },
                    ) { index ->
                        MinePointsRecordItem(record = state.records[index])
                    }
                    if (state.loadMoreStatus.visible) {
                        item {
                            PointsRankLoadMoreFooter(
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
private fun MinePointsToolbar(
    onBackClick: () -> Unit,
    onRankClick: () -> Unit,
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
            text = stringResource(Res.string.my_points),
            color = WanTheme.colors.textPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Center),
        )
        IconButton(
            onClick = onRankClick,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(start = 3.dp, end = 3.dp)
                .size(48.dp),
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_graphic_eq_black_24dp),
                contentDescription = null,
                tint = WanTheme.colors.textPrimary,
                modifier = Modifier.size(22.dp),
            )
        }
        ToolbarBottomShadow(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun MinePointsHeader(
    points: Int,
    levelRank: String,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(WanTheme.colors.backgroundPrimary)
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = points.toString(),
            color = WanTheme.colors.textPrimary,
            fontSize = 64.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 68.sp,
        )
        Text(
            text = levelRank,
            color = WanTheme.colors.textPrimary,
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 16.dp),
        )
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(WanTheme.colors.backgroundSecondary),
    )
}

@Composable
private fun MinePointsRecordItem(
    record: PointRecord,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(WanTheme.colors.backgroundPrimary),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.reason,
                    color = WanTheme.colors.textPrimary,
                    fontSize = 14.sp,
                )
                Text(
                    text = record.date.toDateTime(),
                    color = WanTheme.colors.textSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp),
                )
            }
            Text(
                text = "+${record.coinCount}",
                color = WanTheme.colors.textPrimary,
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 16.dp),
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(WanTheme.colors.backgroundSecondary),
        )
    }
}

@Composable
private fun PointsRankLoadMoreFooter(
    status: HomeLoadMoreStatus,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
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
