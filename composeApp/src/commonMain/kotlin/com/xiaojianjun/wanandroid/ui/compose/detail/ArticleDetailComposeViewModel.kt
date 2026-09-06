package com.xiaojianjun.wanandroid.ui.compose.detail

import com.xiaojianjun.wanandroid.model.store.ReadHistoryStore
import com.xiaojianjun.wanandroid.ui.compose.common.ComposeBaseViewModel
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute
import com.xiaojianjun.wanandroid.ui.compose.navigation.toArticle

class ArticleDetailComposeViewModel : ComposeBaseViewModel() {
    fun saveReadHistory(route: WanRoute.ArticleDetail) {
        if (route.id <= 0L || route.link.isBlank()) return
        launchFlow(
            block = { ReadHistoryStore.addReadHistory(route.toArticle()) },
            showError = false,
        )
    }
}
