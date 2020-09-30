package com.xiaojianjun.wanandroid.model.bean

import androidx.annotation.Keep

/**
 * Created by xiaojianjun on 2019-11-16.
 */
@Keep
data class Frequently(
    val icon: String,
    val id: Int,
    val name: String,
    val link: String,
    val order: Int,
    val visible: Int
)