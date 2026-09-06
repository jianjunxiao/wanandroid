package com.xiaojianjun.wanandroid.ui.compose.home

import androidx.compose.ui.unit.dp
import kotlin.test.Test
import kotlin.test.assertEquals

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
