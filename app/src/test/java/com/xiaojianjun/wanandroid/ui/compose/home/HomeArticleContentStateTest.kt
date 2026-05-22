package com.xiaojianjun.wanandroid.ui.compose.home

import com.xiaojianjun.wanandroid.model.bean.Category
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeArticleContentStateTest {

    @Test
    fun refreshingEmptyListShowsFullLoadingBeforeCategoriesExist() {
        val state = HomeArticleUiState(
            articles = emptyList(),
            categories = emptyList(),
            isRefreshing = true,
        )

        assertTrue(state.shouldShowFullPageLoading())
    }

    @Test
    fun refreshingEmptyListKeepsCategoryRowVisibleAfterCategoriesExist() {
        val state = HomeArticleUiState(
            articles = emptyList(),
            categories = listOf(testCategory()),
            isRefreshing = true,
        )

        assertFalse(state.shouldShowFullPageLoading())
    }

    private fun testCategory() = Category(
        courseId = 0,
        id = 1,
        name = "Android",
        order = 0,
        parentChapterId = 0,
        userControlSetTop = false,
        visible = 1,
        children = mutableListOf(),
    )
}
