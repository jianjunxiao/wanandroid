package com.xiaojianjun.wanandroid.model.bean

/**
 * Created by xiaojianjun on 2019-11-07.
 */
data class Pagination<T>(
    val offset: Int,
    val size: Int,
    val total: Int,
    val pageCount: Int,
    val curPage: Int,
    val over: Boolean,
    val datas: List<T>
)