package com.xiaojianjun.wanandroid.model.bean

import kotlinx.serialization.Serializable

/**
 * Created by xiaojianjun on 2019-11-16.
 */

@Serializable
data class HotWord(
    val id: Int,
    val link: String,
    val order: Int,
    val name: String,
    val visible: Int
)
