package com.xiaojianjun.wanandroid.util

import android.content.Context

/**
 * Created by xiaojianjun on 2019-11-13.
 */
fun dpToPx(context: Context, dp: Float): Float {
    return dp * context.resources.displayMetrics.density
}

fun pxToDp(context: Context, px: Float): Float {
    return px / context.resources.displayMetrics.density
}