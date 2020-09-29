package com.xiaojianjun.wanandroid.util

import android.content.res.Resources

/**
 * 像素密度
 */
internal val density: Float
    get() {
        return Resources.getSystem().displayMetrics.density
    }

/**
 * 字体比例像素密度
 */
internal val scaledDensity: Float
    get() {
        return Resources.getSystem().displayMetrics.scaledDensity
    }
