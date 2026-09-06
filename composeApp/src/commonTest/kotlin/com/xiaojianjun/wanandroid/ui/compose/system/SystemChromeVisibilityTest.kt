package com.xiaojianjun.wanandroid.ui.compose.system

import com.xiaojianjun.wanandroid.model.bean.Category
import kotlin.test.Test
import kotlin.test.assertEquals

class SystemChromeVisibilityTest {

    @Test
    fun hidesCategoryTabsBeforeCategoriesLoad() {
        val state = SystemUiState(isLoading = true)

        assertEquals(false, state.shouldShowCategoryTabs())
    }

    @Test
    fun showsCategoryTabsAfterCategoriesLoad() {
        val state = SystemUiState(categories = listOf(systemCategory()))

        assertEquals(true, state.shouldShowCategoryTabs())
    }

    private fun systemCategory(): Category {
        return Category(
            courseId = 0,
            id = 1,
            name = "体系",
            order = 0,
            parentChapterId = 0,
            userControlSetTop = false,
            visible = 1,
            children = mutableListOf(),
        )
    }
}
