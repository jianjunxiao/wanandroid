package com.xiaojianjun.wanandroid.model.bean

import kotlinx.serialization.Serializable

/**
 * Created by xiaojianjun on 2019-11-15.
 */

@Serializable
data class Navigation(
    val cid: Int,
    val name: String,
    val articles: MutableList<Article>
)
