package com.xiaojianjun.wanandroid.ui.compose.systembars

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.uikit.LocalUIViewController
import platform.UIKit.UIUserInterfaceStyle

@Composable
actual fun ComposeSystemBarsEffect(darkTheme: Boolean) {
    val controller = LocalUIViewController.current
    SideEffect {
        controller.overrideUserInterfaceStyle = if (darkTheme) UIUserInterfaceStyle.UIUserInterfaceStyleDark else UIUserInterfaceStyle.UIUserInterfaceStyleLight
        controller.setNeedsStatusBarAppearanceUpdate()
    }
}
