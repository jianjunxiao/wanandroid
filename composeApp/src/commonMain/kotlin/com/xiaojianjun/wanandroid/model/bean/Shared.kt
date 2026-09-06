package com.xiaojianjun.wanandroid.model.bean

import kotlinx.serialization.Serializable

/**
 * Created by xiaojianjun on 2019-12-03.
 */

@Serializable
data class Shared(
    val coinInfo: PointRank,
    val shareArticles: Pagination<Article>
)
