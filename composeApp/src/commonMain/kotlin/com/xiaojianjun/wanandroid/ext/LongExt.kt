package com.xiaojianjun.wanandroid.ext

import com.xiaojianjun.wanandroid.platform.Platform

fun Long.toDateTime(): String = Platform.formatDate(this, "yyyy-MM-dd HH:mm:ss")
