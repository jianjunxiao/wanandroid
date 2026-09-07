package com.xiaojianjun.wanandroid.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

internal val LocalAppContainer = staticCompositionLocalOf<AppContainer> {
    error("应用依赖尚未初始化")
}

/** 使用当前导航页面的 ViewModelStore，依赖来自应用容器。 */
@Composable
internal inline fun <reified T : ViewModel> appViewModel(
    noinline create: AppContainer.() -> T,
): T {
    val container = LocalAppContainer.current
    return viewModel { container.create() }
}
