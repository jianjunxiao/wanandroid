package com.xiaojianjun.wanandroid.model.bean

import androidx.annotation.Keep

/**
 * Created by xiaojianjun on 2019-11-07.
 */
@Keep
data class Pagination<T>(
    val offset: Int,
    val size: Int,
    val total: Int,
    val pageCount: Int,
    val curPage: Int,
    val over: Boolean,
    val datas: MutableList<T>
)