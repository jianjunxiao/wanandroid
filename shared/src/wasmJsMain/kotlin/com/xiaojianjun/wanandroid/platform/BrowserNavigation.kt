package com.xiaojianjun.wanandroid.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.remember
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.NavigationEventInput
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import androidx.navigationevent.compose.NavigationEventHandler
import androidx.navigationevent.compose.rememberNavigationEventState

@Composable
actual fun PlatformBackHandler(enabled: Boolean, onBack: () -> Unit) {
    NavigationEventHandler(
        state = rememberNavigationEventState(NavigationEventInfo.None),
        isBackEnabled = enabled,
        isForwardEnabled = false,
        onBackCompleted = onBack,
    )
}

/** 浏览器返回与 Compose 弹窗、页面共用同一个返回分发器。 */
@Composable
fun BrowserNavigation() {
    val dispatcher = checkNotNull(
        LocalNavigationEventDispatcherOwner.current
    ).navigationEventDispatcher
    val input = remember { BrowserNavigationInput() }
    DisposableEffect(dispatcher, input) {
        dispatcher.addInput(input)
        registerBrowserBack(input::goBack)
        onDispose {
            removeBrowserBack()
            dispatcher.removeInput(input)
        }
    }
}

private class BrowserNavigationInput : NavigationEventInput() {
    fun goBack() = dispatchOnBackCompleted()
}

@JsFun("""(callback) => {
  if (!window.history.state?.wanandroid) {
    window.history.pushState({wanandroid:true}, '', window.location.href);
  }
  window.__wanBrowserBack = () => {
    if(window.__wanLeaving) { window.__wanLeaving=false; return; }
    callback();
    if(!window.__wanLeaving) window.history.pushState({wanandroid:true}, '', window.location.href);
  };
  window.addEventListener('popstate', window.__wanBrowserBack);
}""")
private external fun registerBrowserBack(callback: () -> Unit)
@JsFun("() => { window.removeEventListener('popstate', window.__wanBrowserBack); delete window.__wanBrowserBack; }")
private external fun removeBrowserBack()
@JsFun("() => { window.__wanLeaving=true; window.history.back(); }")
external fun leaveWebApp()
