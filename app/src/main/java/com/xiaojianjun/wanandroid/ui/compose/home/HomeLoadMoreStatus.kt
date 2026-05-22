package com.xiaojianjun.wanandroid.ui.compose.home

enum class HomeLoadMoreStatus(val visible: Boolean) {
    Complete(visible = false),
    Loading(visible = true),
    Error(visible = true),
    End(visible = true),
}
