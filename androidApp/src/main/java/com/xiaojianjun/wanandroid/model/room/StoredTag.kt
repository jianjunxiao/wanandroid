package com.xiaojianjun.wanandroid.model.room

import androidx.room.Entity
import com.xiaojianjun.wanandroid.model.bean.Tag

/**
 * Created by xiaojianjun on 2019-11-07.
 */
@Entity(tableName = "Tag", primaryKeys = ["articleId", "name", "url"])
data class StoredTag(
    var articleId: Long = 0,
    var name: String = "",
    var url: String = ""
)

fun StoredTag.toTag() = Tag(
    articleId = articleId,
    name = name,
    url = url,
)

fun Tag.toStoredTag() = StoredTag(
    articleId = articleId,
    name = name,
    url = url,
)
