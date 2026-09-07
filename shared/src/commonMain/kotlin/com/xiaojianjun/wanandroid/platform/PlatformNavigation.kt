package com.xiaojianjun.wanandroid.platform

import androidx.compose.runtime.Composable
import androidx.navigation3.runtime.NavEntryDecorator
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute

@Composable
expect fun rememberPlatformViewModelDecorator(): NavEntryDecorator<WanRoute>
