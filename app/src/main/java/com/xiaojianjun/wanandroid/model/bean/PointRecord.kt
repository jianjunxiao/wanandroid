package com.xiaojianjun.wanandroid.model.bean

/**
 * Created by xiaojianjun on 2019-12-02.
 */
data class PointRecord(
    val coinCount: Int,
    val date: Long,
    val desc: String,
    val id: Int,
    val reason: String,
    val type: Int,
    val userId: Int,
    val userName: String
)