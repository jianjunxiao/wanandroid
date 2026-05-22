package com.xiaojianjun.wanandroid.ui.compose.home

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeLoadMoreStatusTest {

    @Test
    fun footerIsVisibleOnlyForLegacyDisplayedStates() {
        assertFalse(HomeLoadMoreStatus.Complete.visible)
        assertTrue(HomeLoadMoreStatus.Loading.visible)
        assertTrue(HomeLoadMoreStatus.Error.visible)
        assertTrue(HomeLoadMoreStatus.End.visible)
    }
}
