package com.xiaojianjun.wanandroid.model.bean

import androidx.annotation.Keep

/**
 * Created by xiaojianjun on 2019-11-22.
 */
@Keep
data class UserInfo(
    val admin: Boolean,
    val email: String,
    val icon: String,
    val id: Int,
    val nickname: String,
    val password: String,
    val publicName: String,
    val token: String,
    val type: Int,
    val username: String,
    val collectIds: MutableList<Long>,
    val chapterTops: MutableList<Any>
)