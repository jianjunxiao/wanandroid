package com.xiaojianjun.wanandroid.ui.compose.home

internal fun HomeArticleUiState.shouldShowFullPageLoading(): Boolean {
    return isRefreshing && articles.isEmpty() && categories.isEmpty()
}
