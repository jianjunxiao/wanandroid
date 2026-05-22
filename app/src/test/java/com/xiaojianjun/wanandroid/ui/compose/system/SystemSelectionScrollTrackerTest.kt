package com.xiaojianjun.wanandroid.ui.compose.system

import org.junit.Assert.assertEquals
import org.junit.Test

class SystemSelectionScrollTrackerTest {

    @Test
    fun firstObservedSelectionDoesNotRequestScroll() {
        val tracker = SystemSelectionScrollTracker()

        assertEquals(false, tracker.shouldScrollToTop(0, 0))
    }

    @Test
    fun unchangedSelectionDoesNotRequestScroll() {
        val tracker = SystemSelectionScrollTracker()

        tracker.shouldScrollToTop(0, 0)

        assertEquals(false, tracker.shouldScrollToTop(0, 0))
    }

    @Test
    fun changedSelectionRequestsScrollOnce() {
        val tracker = SystemSelectionScrollTracker()

        tracker.shouldScrollToTop(0, 0)

        assertEquals(true, tracker.shouldScrollToTop(1, 0))
        assertEquals(false, tracker.shouldScrollToTop(1, 0))
    }
}
