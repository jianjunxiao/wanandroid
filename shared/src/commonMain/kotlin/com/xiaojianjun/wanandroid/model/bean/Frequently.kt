package com.xiaojianjun.wanandroid.model.bean

import kotlinx.serialization.Serializable

/**
 * Created by xiaojianjun on 2019-11-16.
 */

@Serializable
data class Frequently(
    val icon: String,
    val id: Int,
    val name: String,
    val link: String,
    val order: Int,
    val visible: Int
)
