package com.xiaojianjun.wanandroid.ui.compose.home

import kotlin.test.Test
import kotlin.test.assertEquals

class HomeTabAnimationTest {

    @Test
    fun selectionProgressFollowsPagerOffsetBetweenAdjacentTabs() {
        val progress = homeTabSelectionProgress(
            tabCount = 5,
            currentPage = 1,
            currentPageOffsetFraction = 0.4f,
        )

        assertEquals(0f, progress[0], 0.001f)
        assertEquals(0.6f, progress[1], 0.001f)
        assertEquals(0.4f, progress[2], 0.001f)
        assertEquals(0f, progress[3], 0.001f)
        assertEquals(0f, progress[4], 0.001f)
    }

    @Test
    fun selectionProgressHandlesNegativePagerOffsetAfterCurrentPageChanges() {
        val progress = homeTabSelectionProgress(
            tabCount = 5,
            currentPage = 2,
            currentPageOffsetFraction = -0.25f,
        )

        assertEquals(0f, progress[0], 0.001f)
        assertEquals(0.25f, progress[1], 0.001f)
        assertEquals(0.75f, progress[2], 0.001f)
        assertEquals(0f, progress[3], 0.001f)
        assertEquals(0f, progress[4], 0.001f)
    }
}
