package com.xiaojianjun.wanandroid.ext

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by xiaojianjun on 2019-12-02.
 */
fun Long.toDateTime(pattern: String): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(this)