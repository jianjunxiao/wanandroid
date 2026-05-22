package com.xiaojianjun.wanandroid.ui.compose.home

import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Test

class HomeCategoryRowStyleTest {

    @Test
    fun categoryRowKeepsLegacyRecyclerViewElevation() {
        assertEquals(1.dp, LegacyCategoryRowStyle.elevation)
    }

    @Test
    fun categoryRowShadowOverlaysListWithoutTakingLayoutSpace() {
        assertEquals(0.dp, LegacyCategoryRowStyle.bottomShadowLayoutHeight)
    }
}
