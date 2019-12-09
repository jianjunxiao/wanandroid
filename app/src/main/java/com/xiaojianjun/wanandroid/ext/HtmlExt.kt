package com.xiaojianjun.wanandroid.ext

import androidx.core.text.HtmlCompat

/**
 * Created by xiaojianjun on 2019-11-21.
 */
fun String?.htmlToSpanned() =
    if (this.isNullOrEmpty()) "" else HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY)