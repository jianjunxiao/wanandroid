package com.xiaojianjun.wanandroid.ui.compose.navigation

import kotlin.test.Test
import kotlin.test.assertEquals

class WanNavigationStateTest {

    @Test
    fun switchTopLevelRoutesKeepsSeparateStacks() {
        val state = WanNavigationState(
            startRoute = WanRoute.Home,
            topLevelRoutes = setOf(
                WanRoute.Home,
                WanRoute.System,
                WanRoute.Discovery,
                WanRoute.Navigation,
                WanRoute.Profile,
            ),
        )

        state.navigate(WanRoute.ArticleDetail(id = 1L, title = "A", link = "https://example.com/a"))
        state.switchTopLevel(WanRoute.Profile)
        state.navigate(WanRoute.Login)
        state.switchTopLevel(WanRoute.Home)

        assertEquals(
            listOf(
                WanRoute.Profile,
                WanRoute.Login,
                WanRoute.Home,
                WanRoute.ArticleDetail(1L, "A", "https://example.com/a"),
            ),
            state.visibleBackStack(),
        )

        state.switchTopLevel(WanRoute.Profile)

        assertEquals(
            listOf(
                WanRoute.Home,
                WanRoute.ArticleDetail(1L, "A", "https://example.com/a"),
                WanRoute.Profile,
                WanRoute.Login,
            ),
            state.visibleBackStack(),
        )
    }

    @Test
    fun backFromNonHomeTopLevelReturnsHome() {
        val state = WanNavigationState(
            startRoute = WanRoute.Home,
            topLevelRoutes = setOf(WanRoute.Home, WanRoute.Profile),
        )

        state.switchTopLevel(WanRoute.Profile)

        assertEquals(true, state.goBack())
        assertEquals(WanRoute.Home, state.currentTopLevelRoute)
        assertEquals(listOf(WanRoute.Profile, WanRoute.Home), state.visibleBackStack())
    }

    @Test
    fun visitedTopLevelRoutesStayInVisibleBackStackForStateRetention() {
        val state = WanNavigationState(
            startRoute = WanRoute.Home,
            topLevelRoutes = setOf(WanRoute.Home, WanRoute.System),
        )

        state.switchTopLevel(WanRoute.System)
        state.switchTopLevel(WanRoute.Home)

        assertEquals(
            listOf(WanRoute.System, WanRoute.Home),
            state.visibleBackStack(),
        )
    }
}
