package com.xiaojianjun.wanandroid.model.bean

import kotlinx.serialization.Serializable

/**
 * Created by xiaojianjun on 2019-11-07.
 */

@Serializable
data class Tag(
    var articleId: Long = 0,
    var name: String = "",
    var url: String = ""
)
