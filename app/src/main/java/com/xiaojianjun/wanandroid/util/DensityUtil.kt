package com.xiaojianjun.wanandroid.util

import android.content.Context
import android.content.res.Resources

/**
 * Created by xiaojianjun on 2019-11-13.
 */
/**
 * Created by xiaojianjun on 2019-11-13.
 */
fun dpToPx(dp: Float): Float {
    return dp * Resources.getSystem().displayMetrics.density
}

fun pxToDp(px: Float): Float {
    return px / Resources.getSystem().displayMetrics.density
}

fun spToPx(sp: Float): Float {
    return sp * Resources.getSystem().displayMetrics.scaledDensity
}

fun pxToSp(px: Float): Float {
    return px / Resources.getSystem().displayMetrics.scaledDensity
}

fun getDensity(): Float = Resources.getSystem().displayMetrics.density