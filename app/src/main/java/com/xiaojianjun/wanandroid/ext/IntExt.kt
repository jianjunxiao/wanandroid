package com.xiaojianjun.wanandroid.ext

import com.xiaojianjun.wanandroid.util.dpToPx
import com.xiaojianjun.wanandroid.util.pxToDp

/**
 * Created by xiaojianjun on 2019-12-02.
 */
fun Int.dpToPx() = dpToPx(this.toFloat())

fun Int.dpToPxInt() = dpToPx(this.toFloat()).toInt()

fun Int.pxToDp() = pxToDp(this.toFloat())

fun Int.pxToDpInt() = pxToDp(this.toFloat()).toInt()