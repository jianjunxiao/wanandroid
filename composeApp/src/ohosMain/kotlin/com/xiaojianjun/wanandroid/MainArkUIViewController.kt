@file:OptIn(kotlin.experimental.ExperimentalNativeApi::class, kotlinx.cinterop.ExperimentalForeignApi::class)

package com.xiaojianjun.wanandroid

import androidx.compose.ui.napi.JsEnv
import androidx.compose.ui.napi.JsObject
import androidx.compose.ui.window.ComposeArkUIViewController
import com.xiaojianjun.wanandroid.platform.HarmonyHost
import com.xiaojianjun.wanandroid.platform.HarmonyPlatform
import com.xiaojianjun.wanandroid.ui.compose.WanAndroidComposeApp
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.toKString
import kotlinx.coroutines.initMainHandler
import platform.ArkTS.ArkTS_Napi_NativeModule.napi_env
import platform.ArkTS.ArkTS_Napi_NativeModule.napi_value

@CName("WanMainArkUIViewController")
fun WanMainArkUIViewController(
    env: napi_env,
    filesDir: CPointer<ByteVar>,
    cacheDir: CPointer<ByteVar>,
    bridge: napi_value,
): napi_value {
    initMainHandler(env)
    JsEnv.init(env)
    HarmonyPlatform.initialize(filesDir.toKString(), cacheDir.toKString())
    HarmonyHost.bridge = JsObject(bridge)
    return ComposeArkUIViewController(env) {
        WanAndroidComposeApp(onExit = { HarmonyHost.exit() })
    }
}
