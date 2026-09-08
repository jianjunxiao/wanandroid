package com.xiaojianjun.wanandroid.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.navigation3.runtime.NavEntryDecorator
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute

/**
 * 将 iOS / OHOS 返回操作注册到 CPF Compose 宿主提供的分发器。
 *
 * CPF 1.9.2 的宿主使用 Compose BackHandler；不提供新版 NavigationEventHandler
 * 所需的 CompositionLocal。处理器随当前组合注册和释放，遵循宿主的返回事件顺序。
 *
 * @param enabled 是否拦截返回操作；为 false 时交由其他处理器处理。
 * @param onBack 返回操作完成后的回调，重组后使用当前页面提供的最新处理逻辑。
 */
@Composable
@OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class)
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    androidx.compose.ui.backhandler.BackHandler(enabled, onBack)
}

/** 为单个导航条目持有 ViewModelStore，其清理由导航装饰器负责。 */
private class RouteViewModelOwner : ViewModelStoreOwner {
    override val viewModelStore = ViewModelStore()
}

/**
 * 为 iOS 和 OHOS 的导航条目提供稳定的 ViewModelStore。
 *
 * CPF 没有发布 Native 版本的 lifecycle-viewmodel-navigation3，因此沿用现有 OHOS
 * 适配：页面暂时离开组合时保留 ViewModel，条目出栈后清理，整个导航宿主销毁时兜底清理。
 */
@Composable
actual fun rememberPlatformViewModelDecorator(): NavEntryDecorator<WanRoute> {
    val owners = remember { mutableMapOf<Any, RouteViewModelOwner>() }
    DisposableEffect(owners) {
        onDispose {
            owners.values.forEach { it.viewModelStore.clear() }
            owners.clear()
        }
    }
    return remember(owners) {
        NavEntryDecorator(onPop = { key -> owners.remove(key)?.viewModelStore?.clear() }) { entry ->
            val owner = owners.getOrPut(entry.contentKey) { RouteViewModelOwner() }
            CompositionLocalProvider(LocalViewModelStoreOwner provides owner) { entry.Content() }
        }
    }
}
