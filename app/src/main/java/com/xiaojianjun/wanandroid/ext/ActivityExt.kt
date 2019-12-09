package com.xiaojianjun.wanandroid.ext

import android.app.Activity
import android.graphics.Color
import android.view.View.*

/**
 * Created by xiaojianjun on 2019-11-21.
 */
/**
 * 沉浸式系统UI，将系统UI(状态栏和导航栏)设为透明，并且主布局沉浸到系统UI下面。
 * @param [light] true-状态栏字体和图标为黑色，false-状态栏图标为白色
 */
fun Activity.immersiveSystemUi(light: Boolean = true) {
    window.run {
        val mode = if (light) SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or mode
        statusBarColor = Color.TRANSPARENT
        navigationBarColor = Color.TRANSPARENT
    }
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