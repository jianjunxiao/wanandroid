package com.xiaojianjun.wanandroid.ext

import com.xiaojianjun.wanandroid.App
import com.xiaojianjun.wanandroid.util.dpToPx
import com.xiaojianjun.wanandroid.util.pxToDp

/**
 * Created by xiaojianjun on 2019-12-02.
 */
fun Int.toPx() = dpToPx(App.instance, this.toFloat())

fun Int.toIntPx() = dpToPx(App.instance, this.toFloat()).toInt()

fun Int.toDp() = pxToDp(App.instance, this.toFloat())
fun Int.toIntDp() = pxToDp(App.instance, this.toFloat()).toInt()