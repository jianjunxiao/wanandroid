package com.xiaojianjun.wanandroid.ui.compose.systembars

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalView

fun Activity.applyComposeSystemBars(darkTheme: Boolean) {
    window.statusBarColor = Color.TRANSPARENT
    window.navigationBarColor = Color.TRANSPARENT
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        window.isStatusBarContrastEnforced = false
        window.isNavigationBarContrastEnforced = false
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        window.setDecorFitsSystemWindows(false)
        window.decorView.windowInsetsController?.setSystemBarsAppearance(
            if (darkTheme) 0 else lightSystemBarsAppearance(),
            lightSystemBarsAppearance(),
        )
    } else {
        @Suppress("DEPRECATION")
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                (if (darkTheme) {
                    0
                } else {
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or lightNavigationBarFlag()
                })
    }
}

@Composable
fun ComposeSystemBarsEffect(darkTheme: Boolean) {
    val view = LocalView.current
    SideEffect {
        (view.context as? Activity)?.applyComposeSystemBars(darkTheme)
    }
}

private fun lightSystemBarsAppearance(): Int {
    return android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS or
        android.view.WindowInsetsController.APPEARANCE_LIGHT_NAVIGATION_BARS
}

private fun lightNavigationBarFlag(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
    } else {
        0
    }
}
