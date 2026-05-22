package com.xiaojianjun.wanandroid.common.core

import com.xiaojianjun.wanandroid.model.api.ApiService

private const val WANANDROID_WWW_BASE_URL = "https://www.wanandroid.com"

fun String.normalizeWanAndroidImageUrl(): String {
    return if (startsWith(WANANDROID_WWW_BASE_URL)) {
        ApiService.BASE_URL + removePrefix(WANANDROID_WWW_BASE_URL)
    } else {
        this
    }
}

fun String?.normalizedWanAndroidImageUrlOrNull(): String? {
    return this?.normalizeWanAndroidImageUrl()
}
