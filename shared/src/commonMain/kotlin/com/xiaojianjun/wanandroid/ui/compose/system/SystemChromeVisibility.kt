package com.xiaojianjun.wanandroid.ui.compose.system

internal fun SystemUiState.shouldShowCategoryTabs(): Boolean {
    return categories.isNotEmpty()
}
