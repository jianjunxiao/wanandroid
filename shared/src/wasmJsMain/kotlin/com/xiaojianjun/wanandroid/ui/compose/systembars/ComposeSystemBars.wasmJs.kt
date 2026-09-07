package com.xiaojianjun.wanandroid.ui.compose.systembars

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect

@JsFun("(dark) => { document.documentElement.style.colorScheme = dark ? 'dark' : 'light'; document.body.style.background=dark ? '#292929' : '#f8f8f8'; }")
private external fun applyTheme(dark: Boolean)

@Composable
actual fun ComposeSystemBarsEffect(darkTheme: Boolean) { SideEffect { applyTheme(darkTheme) } }
