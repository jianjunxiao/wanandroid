package com.xiaojianjun.wanandroid.ui.compose.systembars

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.xiaojianjun.wanandroid.platform.HarmonyHost

@Composable
actual fun ComposeSystemBarsEffect(darkTheme: Boolean) {
    SideEffect { HarmonyHost.setDarkTheme(darkTheme) }
}
