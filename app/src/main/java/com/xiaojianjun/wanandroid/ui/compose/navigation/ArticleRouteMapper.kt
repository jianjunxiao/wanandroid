package com.xiaojianjun.wanandroid.ui.compose.navigation

import com.xiaojianjun.wanandroid.common.core.MoshiHelper
import com.xiaojianjun.wanandroid.model.bean.Article

fun Article.toArticleDetailRoute() = WanRoute.ArticleDetail(
    id = id,
    title = title.orEmpty(),
    link = link.orEmpty(),
    articleJson = MoshiHelper.toJson(this),
)

fun WanRoute.ArticleDetail.toArticle(): Article {
    return MoshiHelper.fromJson<Article>(articleJson) ?: Article(
        id = id,
        title = title,
        link = link,
    )
}
