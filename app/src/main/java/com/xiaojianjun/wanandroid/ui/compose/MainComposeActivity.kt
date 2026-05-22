package com.xiaojianjun.wanandroid.ui.compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.xiaojianjun.wanandroid.model.store.SettingsStore
import com.xiaojianjun.wanandroid.ui.compose.systembars.applyComposeSystemBars

class MainComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        applyComposeSystemBars(darkTheme = SettingsStore.getNightMode())
        setContent {
            WanAndroidComposeApp(onExit = ::finish)
        }
    }
}
