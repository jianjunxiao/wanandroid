package com.xiaojianjun.wanandroid.ui.compose.opensource

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.resources.*
import com.xiaojianjun.wanandroid.ui.compose.components.ToolbarBottomShadow
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanNavigator
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute
import com.xiaojianjun.wanandroid.ui.compose.theme.WanTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val OpenSourceData = listOf(
    Article(title = "Compose Multiplatform", link = "https://github.com/JetBrains/compose-multiplatform"),
    Article(title = "Kotlin", link = "https://github.com/JetBrains/kotlin"),
    Article(title = "kotlinx.coroutines", link = "https://github.com/Kotlin/kotlinx.coroutines"),
    Article(title = "kotlinx.serialization", link = "https://github.com/Kotlin/kotlinx.serialization"),
    Article(title = "AndroidX", link = "https://github.com/androidx/androidx"),
    Article(title = "Ktor", link = "https://github.com/ktorio/ktor"),
    Article(title = "Coil", link = "https://github.com/coil-kt/coil"),
    Article(title = "OkHttp", link = "https://github.com/square/okhttp"),
    Article(title = "CPF-KMP-CMP", link = "https://gitcode.com/CPF-KMP-CMP"),
    Article(title = "Noto CJK", link = "https://github.com/notofonts/noto-cjk"),
    Article(title = "Noto Emoji", link = "https://github.com/googlefonts/noto-emoji"),
)

@Composable
fun OpenSourceScreen(
    navigator: WanNavigator,
    modifier: Modifier = Modifier,
) {
    OpenSourceContent(
        onBackClick = { navigator.goBack() },
        onItemClick = { article ->
            navigator.navigate(
                WanRoute.ArticleDetail(
                    id = article.id,
                    title = article.title.orEmpty(),
                    link = article.link.orEmpty(),
                ),
            )
        },
        modifier = modifier,
    )
}

@Composable
private fun OpenSourceContent(
    onBackClick: () -> Unit,
    onItemClick: (Article) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WanTheme.colors.backgroundPrimary),
    ) {
        OpenSourceToolbar(onBackClick = onBackClick)
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(WanTheme.colors.backgroundPrimary),
        ) {
            items(
                items = OpenSourceData,
                key = { it.title.orEmpty() },
            ) { article ->
                OpenSourceItem(
                    article = article,
                    onClick = { onItemClick(article) },
                )
            }
        }
    }
}

@Composable
private fun OpenSourceToolbar(
    onBackClick: () -> Unit,
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
            text = stringResource(Res.string.my_open_source),
            color = WanTheme.colors.textPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Center),
        )
        ToolbarBottomShadow(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun OpenSourceItem(
    article: Article,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Text(
            text = article.title.orEmpty(),
            color = WanTheme.colors.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
        )
        Text(
            text = article.link.orEmpty(),
            color = WanTheme.colors.textSecondary,
            fontSize = 13.sp,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, end = 16.dp, bottom = 16.dp),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(WanTheme.colors.backgroundSecondary),
        )
    }
}
