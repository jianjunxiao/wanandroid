package com.xiaojianjun.wanandroid.ui.compose.home

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

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
