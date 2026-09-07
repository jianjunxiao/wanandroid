package com.xiaojianjun.wanandroid.ui.compose.navigation

class WanNavigationState(
    private val startRoute: WanRoute,
    topLevelRoutes: Set<WanRoute>,
) {
    private val topLevelRoutes = topLevelRoutes.toSet()
    private val backStacks = topLevelRoutes.associateWith { mutableListOf(it) }.toMutableMap()
    private val retainedTopLevelRoutes = mutableListOf(startRoute)

    var currentTopLevelRoute: WanRoute = startRoute
        private set

    fun switchTopLevel(route: WanRoute) {
        require(route in topLevelRoutes) { "Route $route is not a top-level route" }
        if (route !in retainedTopLevelRoutes) {
            retainedTopLevelRoutes.add(route)
        }
        currentTopLevelRoute = route
    }

    fun navigate(route: WanRoute) {
        if (route in topLevelRoutes) {
            switchTopLevel(route)
        } else {
            backStacks.getValue(currentTopLevelRoute).add(route)
        }
    }

    fun replace(route: WanRoute) {
        val stack = backStacks.getValue(currentTopLevelRoute)
        if (stack.size == 1) {
            navigate(route)
        } else {
            stack[stack.lastIndex] = route
        }
    }

    fun goBack(): Boolean {
        val currentStack = backStacks.getValue(currentTopLevelRoute)
        return when {
            currentStack.size > 1 -> {
                currentStack.removeAt(currentStack.lastIndex)
                true
            }
            currentTopLevelRoute != startRoute -> {
                currentTopLevelRoute = startRoute
                true
            }
            else -> false
        }
    }

    fun visibleBackStack(): List<WanRoute> {
        return buildList {
            retainedTopLevelRoutes
                .filterNot { it == currentTopLevelRoute }
                .forEach { route -> addAll(backStacks.getValue(route)) }
            addAll(backStacks.getValue(currentTopLevelRoute))
        }
    }
}
