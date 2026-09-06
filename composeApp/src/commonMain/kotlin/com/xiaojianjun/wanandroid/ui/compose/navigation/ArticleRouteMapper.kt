package com.xiaojianjun.wanandroid.ui.compose.navigation

import com.xiaojianjun.wanandroid.common.core.JsonCodec
import com.xiaojianjun.wanandroid.model.bean.Article

fun Article.toArticleDetailRoute() = WanRoute.ArticleDetail(
    id = id,
    title = title.orEmpty(),
    link = link.orEmpty(),
    articleJson = JsonCodec.toJson(this),
)

fun WanRoute.ArticleDetail.toArticle(): Article {
    return JsonCodec.fromJson<Article>(articleJson) ?: Article(
        id = id,
        title = title,
        link = link,
    )
}
