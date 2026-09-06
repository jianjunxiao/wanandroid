package com.xiaojianjun.wanandroid.model.bean

import kotlinx.serialization.Serializable

/**
 * Created by xiaojianjun on 2019-12-02.
 */

@Serializable
data class PointRank(
    val coinCount: Int,
    val level: Int,
    val rank: Int,
    val userId: Int,
    val username: String
)
