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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.model.bean.PointRank
import com.xiaojianjun.wanandroid.ui.compose.components.ReloadContent
import com.xiaojianjun.wanandroid.ui.compose.components.ToolbarBottomShadow
import com.xiaojianjun.wanandroid.ui.compose.home.HomeLoadMoreStatus
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanNavigator
import com.xiaojianjun.wanandroid.ui.compose.theme.WanTheme
import kotlinx.coroutines.launch

@Composable
fun PointsRankScreen(
    navigator: WanNavigator,
    modifier: Modifier = Modifier,
    viewModel: PointsRankComposeViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WanTheme.colors.backgroundSecondary),
    ) {
        PointsRankToolbar(
            onBackClick = { navigator.goBack() },
            onTitleClick = {
                scope.launch { listState.animateScrollToItem(0) }
            },
        )
        PointsRankContent(
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
private fun PointsRankContent(
    state: PointsRankUiState,
    onReload: () -> Unit,
    onLoadMore: () -> Unit,
    listState: androidx.compose.foundation.lazy.LazyListState,
    modifier: Modifier = Modifier,
) {
    when {
        state.showReload -> ReloadContent(onReload = onReload, modifier = modifier)
        else -> {
            val shouldLoadMore by remember(state.ranks.size, state.loadMoreStatus) {
                derivedStateOf {
                    val lastVisible = listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index
                    state.loadMoreStatus == HomeLoadMoreStatus.Complete &&
                        lastVisible != null &&
                        lastVisible >= state.ranks.lastIndex - 2
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
                    items(
                        count = state.ranks.size,
                        key = { state.ranks[it].userId },
                    ) { index ->
                        PointsRankItem(
                            no = index + 1,
                            rank = state.ranks[index],
                        )
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
private fun PointsRankToolbar(
    onBackClick: () -> Unit,
    onTitleClick: () -> Unit,
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
                painter = painterResource(R.drawable.ic_arrow_back_black_24dp),
                contentDescription = null,
                tint = WanTheme.colors.textPrimary,
                modifier = Modifier.size(24.dp),
            )
        }
        Text(
            text = stringResource(R.string.my_points_rank),
            color = WanTheme.colors.textPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.Center)
                .clickable(onClick = onTitleClick),
        )
        ToolbarBottomShadow(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun PointsRankItem(
    no: Int,
    rank: PointRank,
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
                .height(53.dp)
                .padding(start = 16.dp, end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = no.toString(),
                color = WanTheme.colors.textPrimary,
                fontSize = 14.sp,
            )
            Text(
                text = rank.username,
                color = WanTheme.colors.textPrimary,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f),
            )
            Text(
                text = rank.coinCount.toString(),
                color = WanTheme.colors.textPrimary,
                fontSize = 14.sp,
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
