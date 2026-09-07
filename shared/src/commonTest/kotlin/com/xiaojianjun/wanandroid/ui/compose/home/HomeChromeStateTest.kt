package com.xiaojianjun.wanandroid.ui.compose.home

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HomeChromeStateTest {

    @Test
    fun scrollDirectionControlsBottomBarVisibilityLikeLegacyAppBarOffset() {
        val state = HomeChromeState()

        assertTrue(state.bottomBarVisible)

        state.onToolbarOffsetChanged(-12)
        assertFalse(state.bottomBarVisible)

        state.onToolbarOffsetChanged(-6)
        assertTrue(state.bottomBarVisible)
    }
}
