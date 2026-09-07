package com.xiaojianjun.wanandroid.ui.compose.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xiaojianjun.wanandroid.di.appViewModel
import com.xiaojianjun.wanandroid.model.store.SettingsStore
import com.xiaojianjun.wanandroid.platform.ArticleToolbarAction
import com.xiaojianjun.wanandroid.platform.ArticleWebView
import com.xiaojianjun.wanandroid.resources.*
import com.xiaojianjun.wanandroid.ui.compose.components.ToolbarBottomShadow
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanNavigator
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute
import com.xiaojianjun.wanandroid.ui.compose.theme.WanTheme
import org.jetbrains.compose.resources.painterResource

@Composable
fun ArticleDetailScreen(
    route: WanRoute.ArticleDetail,
    navigator: WanNavigator,
    modifier: Modifier = Modifier,
    viewModel: ArticleDetailComposeViewModel = appViewModel { ArticleDetailComposeViewModel(historyRepository) },
) {
    LaunchedEffect(route.id, route.link) {
        viewModel.saveReadHistory(route)
    }

    androidx.compose.foundation.layout.Column(
        modifier = modifier
            .fillMaxSize()
            .background(WanTheme.colors.backgroundPrimary),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(WanTheme.colors.backgroundPrimary),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(48.dp)
                    .clickable { navigator.goBack() },
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back_black_24dp),
                    contentDescription = null,
                    tint = WanTheme.colors.textPrimary,
                    modifier = Modifier.size(24.dp),
                )
            }
            Text(
                text = route.title,
                color = WanTheme.colors.textPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            ArticleToolbarAction(url = route.link, modifier = Modifier.size(52.dp))
        }
        ToolbarBottomShadow()

        ArticleWebView(
            url = route.link,
            textZoom = SettingsStore.getWebTextZoom(),
            modifier = Modifier.fillMaxWidth().weight(1f),
        )
    }
}
