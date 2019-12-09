package com.xiaojianjun.wanandroid.model.bean

/**
 * Created by xiaojianjun on 2019-11-16.
 */
data class Banner(
    val desc: String,
    val id: Int,
    val imagePath: String,
    val isVisible: Int,
    val order: Int,
    val title: String,
    val type: Int,
    val url: String
)