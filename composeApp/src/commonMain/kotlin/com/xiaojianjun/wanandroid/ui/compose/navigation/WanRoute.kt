package com.xiaojianjun.wanandroid.ui.compose.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface WanRoute : NavKey {
    @Serializable
    data object Home : WanRoute

    @Serializable
    data object System : WanRoute

    @Serializable
    data object Discovery : WanRoute

    @Serializable
    data object Navigation : WanRoute

    @Serializable
    data object Profile : WanRoute

    @Serializable
    data object Login : WanRoute

    @Serializable
    data object Register : WanRoute

    @Serializable
    data object Settings : WanRoute

    @Serializable
    data object OpenSource : WanRoute

    @Serializable
    data object ShareArticle : WanRoute

    @Serializable
    data object PointsRank : WanRoute

    @Serializable
    data object MinePoints : WanRoute

    @Serializable
    data object SharedArticles : WanRoute

    @Serializable
    data object Collection : WanRoute

    @Serializable
    data object History : WanRoute

    @Serializable
    data class Search(
        val initialKeywords: String = "",
    ) : WanRoute

    @Serializable
    data class ArticleDetail(
        val id: Long,
        val title: String,
        val link: String,
        val articleJson: String = "",
    ) : WanRoute
}
