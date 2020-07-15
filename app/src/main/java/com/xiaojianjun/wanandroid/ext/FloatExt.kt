package com.xiaojianjun.wanandroid.ext

import com.xiaojianjun.wanandroid.util.dpToPx
import com.xiaojianjun.wanandroid.util.pxToDp

/**
 * Created by xiaojianjun on 2019-12-02.
 */
fun Float.dpToPx() = dpToPx(this)

fun Float.dpToPxInt() = dpToPx(this).toInt()

fun Float.pxToDp() = pxToDp(this)

fun Float.pxToDpInt() = pxToDp(this).toInt()