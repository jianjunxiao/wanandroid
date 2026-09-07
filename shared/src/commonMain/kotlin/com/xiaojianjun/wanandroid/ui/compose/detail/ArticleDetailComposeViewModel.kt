package com.xiaojianjun.wanandroid.ui.compose.detail

import com.xiaojianjun.wanandroid.model.repository.HistoryRepository
import com.xiaojianjun.wanandroid.ui.compose.common.ComposeBaseViewModel
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute
import com.xiaojianjun.wanandroid.ui.compose.navigation.toArticle

class ArticleDetailComposeViewModel(
    private val historyRepository: HistoryRepository,
) : ComposeBaseViewModel() {
    fun saveReadHistory(route: WanRoute.ArticleDetail) {
        if (route.id <= 0L || route.link.isBlank()) return
        launchFlow(
            block = { historyRepository.addHistory(route.toArticle()) },
            showError = false,
        )
    }
}
