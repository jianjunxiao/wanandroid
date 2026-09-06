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

private class RouteViewModelOwner : ViewModelStoreOwner {
    override val viewModelStore = ViewModelStore()
}

@Composable
actual fun rememberPlatformViewModelDecorator(): NavEntryDecorator<WanRoute> {
    // 鸿蒙没有发布 lifecycle-viewmodel-navigation3，按 NavEntry 生命周期管理相同的 ViewModelStore。
    val owners = remember { mutableMapOf<Any, RouteViewModelOwner>() }
    DisposableEffect(owners) {
        onDispose { owners.values.forEach { it.viewModelStore.clear() }; owners.clear() }
    }
    return remember(owners) {
        NavEntryDecorator(onPop = { key -> owners.remove(key)?.viewModelStore?.clear() }) { entry ->
            val owner = owners.getOrPut(entry.contentKey) { RouteViewModelOwner() }
            CompositionLocalProvider(LocalViewModelStoreOwner provides owner) { entry.Content() }
        }
    }
}
