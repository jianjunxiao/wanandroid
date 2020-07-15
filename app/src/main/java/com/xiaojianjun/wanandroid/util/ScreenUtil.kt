package com.xiaojianjun.wanandroid.util

import android.content.Context

/**
 * Created by xiaojianjun on 2019-11-17.
 */

/**
 * 获取屏幕高度
 */
fun getSreenHeight(context: Context) = context.resources.displayMetrics.heightPixels

/**
 * 获取屏幕宽度
 */
fun getSreenWidth(context: Context) = context.resources.displayMetrics.widthPixels

/**
 * 获取状态栏高度
 */
fun getStatusBarHeight(context: Context): Int {
    var statusBarHeight = 0
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
    }
    return statusBarHeight
}