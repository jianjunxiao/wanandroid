package com.xiaojianjun.wanandroid.ui.compose.navigation

class WanNavigator(
    private val state: WanNavigationState,
    private val onChanged: () -> Unit,
) {
    val currentTopLevelRoute: WanRoute
        get() = state.currentTopLevelRoute

    val backStack: List<WanRoute>
        get() = state.visibleBackStack()

    fun switchTopLevel(route: WanRoute) {
        state.switchTopLevel(route)
        onChanged()
    }

    fun navigate(route: WanRoute) {
        state.navigate(route)
        onChanged()
    }

    fun replace(route: WanRoute) {
        state.replace(route)
        onChanged()
    }

    fun goBack(): Boolean {
        val handled = state.goBack()
        if (handled) onChanged()
        return handled
    }
}
