package com.xiaojianjun.wanandroid.ui.compose.home

import kotlin.math.abs

internal fun homeTabSelectionProgress(
    tabCount: Int,
    currentPage: Int,
    currentPageOffsetFraction: Float,
): List<Float> {
    val selectedPosition = (currentPage + currentPageOffsetFraction)
        .coerceIn(0f, (tabCount - 1).coerceAtLeast(0).toFloat())

    return List(tabCount) { index ->
        (1f - abs(index - selectedPosition)).coerceIn(0f, 1f)
    }
}
