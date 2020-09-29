package com.xiaojianjun.wanandroid.ext

import android.annotation.TargetApi
import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View.*
import android.view.WindowInsetsController
import android.view.WindowManager


/**
 * 沉浸式系统UI，将系统状态栏设为透明，并且主布局沉浸到系统状态栏下面。
 * @param [light] true-状态栏字体和图标为黑色，false-状态栏图标为白色
 */
fun Activity.immersiveStatusBar(light: Boolean = true) {
    if (Build.VERSION.SDK_INT >= 30) {
        setDecorFitsSystemWindows(false)
        setStatusBarAppearance(light)
        setStatusBarColor(Color.TRANSPARENT)
    } else {
        window.run {
            clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            decorView.systemUiVisibility =
                if (light) {
                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE or
                            SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or SYSTEM_UI_FLAG_LAYOUT_STABLE
                }
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
        }
    }
}

/**
 * 设置浅色状态栏
 */
@TargetApi(Build.VERSION_CODES.R)
fun Activity.setLightStatusBar() {
    setStatusBarAppearance(true)
}

/**
 * 设置深色状态栏
 */
@TargetApi(Build.VERSION_CODES.R)
fun Activity.setDarkStatusBar() {
    setStatusBarAppearance(false)
}

/**
 * 设置状态栏外观
 * @param light 是否是浅色模式 true-浅色模式，false-深色模式
 */
@TargetApi(Build.VERSION_CODES.R)
fun Activity.setStatusBarAppearance(light: Boolean) {
    window.decorView.windowInsetsController?.setSystemBarsAppearance(
        if (light) {
            0
        } else {
            WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
        },
        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
    )
}

/**
 * 设置decor设置状态栏是否不盖住内容
 * @param decorFitsSystemWindows true-不盖住内容，false-盖住内容
 */
@TargetApi(Build.VERSION_CODES.R)
fun Activity.setDecorFitsSystemWindows(decorFitsSystemWindows: Boolean) {
    window.setDecorFitsSystemWindows(decorFitsSystemWindows)
}

/**
 * 设置状态栏颜色
 */
fun Activity.setStatusBarColor(color: Int) {
    window.statusBarColor = color
}

/**
 * 设置导航栏颜色
 */
fun Activity.setNavigationBarColor(color: Int) {
    window.navigationBarColor = color
}

/**
 * 获取Activity的亮度
 * @return 0 ~ 1
 */
fun Activity.getBrightness() = window.attributes.screenBrightness

/**
 * 设置Activity的亮度
 * @param [brightness] 0 ~ 1
 */
fun Activity.setBrightness(brightness: Float) {
    val attributes = window.attributes
    attributes.screenBrightness = brightness
    window.attributes = attributes
}