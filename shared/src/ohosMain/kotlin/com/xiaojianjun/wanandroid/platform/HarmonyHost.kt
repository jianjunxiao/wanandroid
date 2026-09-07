@file:OptIn(kotlinx.cinterop.ExperimentalForeignApi::class)

package com.xiaojianjun.wanandroid.platform

import androidx.compose.ui.napi.JsObject

internal object HarmonyHost {
    lateinit var bridge: JsObject
    fun setDarkTheme(dark: Boolean) { bridge.call(if (dark) "dark" else "light") }
    fun exit() { bridge.call("exit") }
}
