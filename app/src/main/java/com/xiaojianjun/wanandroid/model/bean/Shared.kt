package com.xiaojianjun.wanandroid.model.bean

/**
 * Created by xiaojianjun on 2019-12-03.
 */
data class Shared(
    val coinInfo: PointRank,
    val shareArticles: Pagination<Article>
)