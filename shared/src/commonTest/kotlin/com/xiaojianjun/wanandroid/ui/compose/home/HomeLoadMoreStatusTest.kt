package com.xiaojianjun.wanandroid.ui.compose.home

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HomeLoadMoreStatusTest {

    @Test
    fun footerIsVisibleOnlyForLegacyDisplayedStates() {
        assertFalse(HomeLoadMoreStatus.Complete.visible)
        assertTrue(HomeLoadMoreStatus.Loading.visible)
        assertTrue(HomeLoadMoreStatus.Error.visible)
        assertTrue(HomeLoadMoreStatus.End.visible)
    }
}
