package com.xiaojianjun.wanandroid.ui.compose.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.common.core.normalizeWanAndroidImageUrl
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.ui.compose.common.htmlPlainText
import com.xiaojianjun.wanandroid.ui.compose.theme.WanTheme

@Composable
fun ArticleCard(
    article: Article,
    onClick: () -> Unit,
    onCollectClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable(onClick = onClick),
        color = WanTheme.colors.backgroundPrimary,
        shadowElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier.padding(bottom = 8.dp),
        ) {
            ArticleHeader(article = article)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 12.dp, end = 16.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = article.title.htmlPlainText(),
                        color = WanTheme.colors.textPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                    )
                    val desc = article.desc.htmlPlainText()
                    if (desc.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(top = 8.dp),
                            text = desc,
                            color = WanTheme.colors.textSecondary,
                            fontSize = 13.sp,
                            lineHeight = 15.sp,
                            maxLines = 3,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                val imageUrl = article.envelopePic.orEmpty().normalizeWanAndroidImageUrl()
                if (imageUrl.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(12.dp))
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(78.dp)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(1.dp))
                            .background(WanTheme.colors.backgroundThird),
                    )
                }
            }
            ArticleFooter(
                article = article,
                onCollectClick = onCollectClick,
            )
        }
    }
}

@Composable
fun SimpleArticleCard(
    article: Article,
    onClick: () -> Unit,
    onCollectClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
            .clickable(onClick = onClick),
        color = WanTheme.colors.backgroundPrimary,
        shadowElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier.padding(bottom = 8.dp),
        ) {
            Text(
                modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp),
                text = article.title.htmlPlainText(),
                color = WanTheme.colors.textPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 4.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (article.fresh) {
                        Text(
                            text = stringResource(R.string.fresh),
                            color = WanTheme.colors.badge,
                            fontSize = 12.sp,
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = article.displayAuthor(),
                        color = WanTheme.colors.textPrimary,
                        fontSize = 12.sp,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = article.niceDate.orEmpty(),
                        color = WanTheme.colors.textThird,
                        fontSize = 12.sp,
                    )
                }
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable(onClick = onCollectClick),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        painter = painterResource(
                            if (article.collect) R.drawable.ic_star_black_24dp
                            else R.drawable.ic_star_border_black_24dp
                        ),
                        contentDescription = null,
                        tint = WanTheme.colors.textThird,
                        modifier = Modifier.size(20.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun ArticleHeader(article: Article) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (article.top) {
                Text(
                    text = stringResource(R.string.top),
                    color = WanTheme.colors.badge,
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = article.displayAuthor(),
                color = WanTheme.colors.textPrimary,
                fontSize = 12.sp,
            )
            val tag = article.tags.firstOrNull()?.name.orEmpty()
            if (tag.isNotEmpty()) {
                Spacer(modifier = Modifier.width(8.dp))
                ArticleTag(text = tag)
            }
        }
        Text(
            modifier = Modifier.align(Alignment.CenterEnd),
            text = article.displayChapter(),
            color = WanTheme.colors.textThird,
            fontSize = 12.sp,
        )
    }
}

@Composable
private fun ArticleTag(text: String) {
    Text(
        text = text,
        color = WanTheme.colors.textPrimary,
        fontSize = 10.sp,
        style = TextStyle(
            platformStyle = PlatformTextStyle(includeFontPadding = true),
        ),
        modifier = Modifier
            .background(WanTheme.colors.backgroundPrimary, RoundedCornerShape(2.dp))
            .border(BorderStroke(0.8.dp, WanTheme.colors.textPrimary), RoundedCornerShape(2.dp))
            .padding(start = 3.dp, top = 1.dp, end = 3.dp, bottom = 1.5.dp),
    )
}

@Composable
private fun ArticleFooter(
    article: Article,
    onCollectClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 12.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (article.fresh && !article.top) {
                Text(
                    text = stringResource(R.string.fresh),
                    color = WanTheme.colors.badge,
                    fontSize = 12.sp,
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text(
                text = article.niceDate.orEmpty(),
                color = WanTheme.colors.textThird,
                fontSize = 12.sp,
            )
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .clickable(onClick = onCollectClick),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                painter = painterResource(
                    if (article.collect) R.drawable.ic_star_black_24dp
                    else R.drawable.ic_star_border_black_24dp
                ),
                contentDescription = null,
                tint = WanTheme.colors.textThird,
                modifier = Modifier.size(20.dp),
            )
        }
    }
}

private fun Article.displayAuthor(): String {
    return when {
        !author.isNullOrEmpty() -> author.orEmpty()
        !shareUser.isNullOrEmpty() -> shareUser.orEmpty()
        else -> "匿名"
    }
}

private fun Article.displayChapter(): String {
    return when {
        !superChapterName.isNullOrEmpty() && !chapterName.isNullOrEmpty() ->
            "${superChapterName.htmlPlainText()}/${chapterName.htmlPlainText()}"
        superChapterName.isNullOrEmpty() && !chapterName.isNullOrEmpty() ->
            chapterName.htmlPlainText()
        !superChapterName.isNullOrEmpty() && chapterName.isNullOrEmpty() ->
            superChapterName.htmlPlainText()
        else -> ""
    }
}
