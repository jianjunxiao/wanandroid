package com.xiaojianjun.wanandroid.platform

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntryDecorator
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute

@Composable
actual fun rememberPlatformViewModelDecorator(): NavEntryDecorator<WanRoute> =
    rememberViewModelStoreNavEntryDecorator()
