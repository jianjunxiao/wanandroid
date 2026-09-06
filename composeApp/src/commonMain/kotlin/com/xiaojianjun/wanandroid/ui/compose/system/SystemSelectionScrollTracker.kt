package com.xiaojianjun.wanandroid.ui.compose.system

internal class SystemSelectionScrollTracker {
    private var lastSelection: SystemSelection? = null

    fun shouldScrollToTop(parent: Int, child: Int): Boolean {
        val selection = SystemSelection(parent, child)
        val previousSelection = lastSelection
        lastSelection = selection
        return previousSelection != null && previousSelection != selection
    }
}

private data class SystemSelection(
    val parent: Int,
    val child: Int,
)
