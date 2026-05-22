package com.xiaojianjun.wanandroid.ui.compose.profile

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanNavigator
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute
import com.xiaojianjun.wanandroid.ui.compose.navigation.toArticleDetailRoute
import com.xiaojianjun.wanandroid.ui.compose.theme.WanTheme

@Composable
fun ProfileScreen(
    navigator: WanNavigator,
    modifier: Modifier = Modifier,
    viewModel: ProfileComposeViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner, viewModel) {
        val lifecycleObserver = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshLoginState()
            }
        }
        val loginObserver = Observer<Boolean> {
            viewModel.refreshLoginState()
        }
        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)
        Bus.observe(USER_LOGIN_STATE_CHANGED, lifecycleOwner, loginObserver)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    ProfileContent(
        state = state,
        onLoginRequired = { navigator.navigate(WanRoute.Login) },
        onOpenArticle = { article -> navigator.openArticle(article) },
        onOpenSource = { navigator.navigate(WanRoute.OpenSource) },
        onOpenSettings = { navigator.navigate(WanRoute.Settings) },
        onOpenPointsRank = { navigator.navigate(WanRoute.PointsRank) },
        onOpenMinePoints = { navigator.navigate(WanRoute.MinePoints) },
        onOpenSharedArticles = { navigator.navigate(WanRoute.SharedArticles) },
        onOpenCollection = { navigator.navigate(WanRoute.Collection) },
        onOpenHistory = { navigator.navigate(WanRoute.History) },
        modifier = modifier,
    )
}

@Composable
private fun ProfileContent(
    state: ProfileUiState,
    onLoginRequired: () -> Unit,
    onOpenArticle: (Article) -> Unit,
    onOpenSource: () -> Unit,
    onOpenSettings: () -> Unit,
    onOpenPointsRank: () -> Unit,
    onOpenMinePoints: () -> Unit,
    onOpenSharedArticles: () -> Unit,
    onOpenCollection: () -> Unit,
    onOpenHistory: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WanTheme.colors.backgroundSecondary),
    ) {
        ProfileToolbar()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 64.dp),
        ) {
            ProfileHeader(
                state = state,
                onClick = { requireLogin(onLoginRequired) { } },
                modifier = Modifier.padding(top = 8.dp),
            )
            ProfileSection(modifier = Modifier.padding(top = 8.dp)) {
                ProfileMenuItem(
                    iconRes = R.drawable.ic_my_integral,
                    title = stringResource(R.string.my_points),
                    onClick = { requireLogin(onLoginRequired, onOpenMinePoints) },
                )
                MenuDivider()
                ProfileMenuItem(
                    iconRes = R.drawable.ic_graphic_eq_black_24dp,
                    title = stringResource(R.string.my_points_rank),
                    onClick = onOpenPointsRank,
                )
            }
            ProfileSection(modifier = Modifier.padding(top = 8.dp)) {
                ProfileMenuItem(
                    iconRes = R.drawable.ic_add_circle_outline_black_24dp,
                    title = stringResource(R.string.my_share),
                    onClick = { requireLogin(onLoginRequired, onOpenSharedArticles) },
                )
                MenuDivider()
                ProfileMenuItem(
                    iconRes = R.drawable.ic_star_border_black_24dp,
                    title = stringResource(R.string.my_collect),
                    onClick = { requireLogin(onLoginRequired, onOpenCollection) },
                )
                MenuDivider()
                ProfileMenuItem(
                    iconRes = R.drawable.ic_history_black_24dp,
                    title = stringResource(R.string.my_view_history),
                    onClick = onOpenHistory,
                )
            }
            ProfileSection(modifier = Modifier.padding(top = 8.dp)) {
                ProfileMenuItem(
                    iconRes = R.drawable.ic_github,
                    title = stringResource(R.string.my_open_source),
                    onClick = onOpenSource,
                )
                MenuDivider()
                val aboutAuthor = stringResource(R.string.my_about_author)
                ProfileMenuItem(
                    iconRes = R.drawable.ic_info_outline_black_24dp,
                    title = aboutAuthor,
                    onClick = {
                        onOpenArticle(
                            Article(
                                title = aboutAuthor,
                                link = "https://github.com/jianjunxiao",
                            ),
                        )
                    },
                )
                MenuDivider()
                ProfileMenuItem(
                    iconRes = R.drawable.ic_settings_black_24dp,
                    title = stringResource(R.string.my_system_setting),
                    onClick = onOpenSettings,
                )
            }
        }
    }
}

@Composable
private fun ProfileToolbar(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(WanTheme.colors.backgroundPrimary),
    ) {
        Text(
            text = stringResource(R.string.mine),
            color = WanTheme.colors.textPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Center),
        )
        ToolbarBottomShadow(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun ProfileHeader(
    state: ProfileUiState,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp)
            .background(WanTheme.colors.backgroundPrimary)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_avatar_black_96dp),
            contentDescription = null,
            tint = WanTheme.colors.textThird,
            modifier = Modifier.size(48.dp),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
        ) {
            if (state.isLogin && state.userInfo != null) {
                Text(
                    text = state.userInfo.nickname,
                    color = WanTheme.colors.textPrimary,
                    fontSize = 14.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "ID: ${state.userInfo.id}",
                    color = WanTheme.colors.textSecondary,
                    fontSize = 12.sp,
                )
            } else {
                Text(
                    text = stringResource(R.string.login_register),
                    color = WanTheme.colors.textPrimary,
                    fontSize = 16.sp,
                )
            }
        }
        Icon(
            painter = painterResource(R.drawable.ic_chevron_right_black_24dp),
            contentDescription = null,
            tint = WanTheme.colors.textThird,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun ProfileSection(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp)
            .background(WanTheme.colors.backgroundPrimary),
        content = content,
    )
}

@Composable
private fun ProfileMenuItem(
    iconRes: Int,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(start = 16.dp, top = 14.dp, end = 16.dp, bottom = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = WanTheme.colors.textPrimary,
            modifier = Modifier.size(20.dp),
        )
        Text(
            text = title,
            color = WanTheme.colors.textPrimary,
            fontSize = 14.sp,
            modifier = Modifier.padding(start = 16.dp),
        )
    }
}

@Composable
private fun MenuDivider(
    modifier: Modifier = Modifier,
) {
    Spacer(
        modifier = modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(WanTheme.colors.backgroundSecondary),
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

private fun requireLogin(
    onLoginRequired: () -> Unit,
    block: () -> Unit,
) {
    if (isLogin()) block() else onLoginRequired()
}

private fun WanNavigator.openArticle(article: Article) {
    navigate(article.toArticleDetailRoute())
}
