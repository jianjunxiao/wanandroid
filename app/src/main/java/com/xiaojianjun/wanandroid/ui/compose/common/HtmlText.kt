package com.xiaojianjun.wanandroid.ui.compose.common

import androidx.core.text.HtmlCompat

fun String?.htmlPlainText(): String {
    return if (isNullOrEmpty()) {
        ""
    } else {
        HtmlCompat.fromHtml(this, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
    }
}
