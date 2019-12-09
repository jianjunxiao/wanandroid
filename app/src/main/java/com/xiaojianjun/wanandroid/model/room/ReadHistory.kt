package com.xiaojianjun.wanandroid.model.room

import androidx.room.Embedded
import androidx.room.Relation
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.bean.Tag

/**
 * Created by xiaojianjun on 2019-12-05.
 */
data class ReadHistory(
    @Embedded
    var article: Article,
    @Relation(parentColumn = "id", entityColumn = "article_id")
    var tags: List<Tag>
)