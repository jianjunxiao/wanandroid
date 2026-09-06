@file:OptIn(androidx.compose.ui.ExperimentalComposeUiApi::class, androidx.compose.ui.text.ExperimentalTextApi::class, org.jetbrains.compose.resources.ExperimentalResourceApi::class)

package com.xiaojianjun.wanandroid

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFontFamilyResolver
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.window.ComposeViewport
import com.xiaojianjun.wanandroid.platform.BrowserNavigation
import com.xiaojianjun.wanandroid.platform.leaveWebApp
import com.xiaojianjun.wanandroid.resources.Res
import com.xiaojianjun.wanandroid.ui.compose.WanAndroidComposeApp
import kotlinx.browser.document
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

@JsFun("() => { document.getElementById('startup-error').hidden = false; }")
private external fun showStartupError()

fun main() {
    ComposeViewport(document.getElementById("composeApp")!!) {
        BrowserNavigation()
        val resolver = LocalFontFamilyResolver.current
        var fontsReady by remember { mutableStateOf(false) }
        LaunchedEffect(resolver) {
            // Web 的 Skia 无法直接使用系统字体，预加载中文和 Emoji 回退字体。
            try {
                coroutineScope {
                    val chinese = async { Res.readBytes("font/noto_sans_sc_regular.otf") }
                    val emoji = async { Res.readBytes("font/noto_color_emoji.ttf") }
                    resolver.preload(FontFamily(Font("WanNotoSansSC", chinese.await())))
                    resolver.preload(FontFamily(Font("WanNotoEmoji", emoji.await())))
                }
                fontsReady = true
            } catch (error: Exception) {
                if (error is CancellationException) throw error
                showStartupError()
            }
        }
        if (fontsReady) WanAndroidComposeApp(onExit = ::leaveWebApp)
        else Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
    }
}
