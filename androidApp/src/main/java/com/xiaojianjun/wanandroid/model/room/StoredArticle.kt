package com.xiaojianjun.wanandroid.model.room

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.xiaojianjun.wanandroid.model.bean.Article

/**
 * Created by xiaojianjun on 2019-09-18.
 */
@Entity(tableName = "Article")
data class StoredArticle(
    var apkLink: String? = "",
    var audit: Int = 0,
    var author: String? = "",
    var chapterId: Int = 0,
    var chapterName: String? = "",
    var collect: Boolean = false,
    var courseId: Int = 0,
    var desc: String? = "",
    var envelopePic: String? = "",
    var fresh: Boolean = false,
    @PrimaryKey
    var id: Long = 0,
    var link: String? = "",
    var niceDate: String? = "",
    var niceShareDate: String? = "",
    var origin: String? = "",
    var originId: Long = 0,
    var prefix: String? = "",
    var projectLink: String? = "",
    var publishTime: Long = 0,
    var selfVisible: Int = 0,
    var shareDate: Long? = 0,
    var shareUser: String? = "",
    var superChapterId: Int = 0,
    var superChapterName: String? = "",
    @Ignore
    var tags: MutableList<com.xiaojianjun.wanandroid.model.bean.Tag> = mutableListOf(),
    var title: String? = "",
    var type: Int = 0,
    var userId: Int = 0,
    var visible: Int = 0,
    var zan: Int = 0,
    var top: Boolean = false,
    // 历史记录sqlite数据库专用字段，阅读时间
    var readTime: Long = 0L
)

fun StoredArticle.toArticle() = Article(
    apkLink = apkLink,
    audit = audit,
    author = author,
    chapterId = chapterId,
    chapterName = chapterName,
    collect = collect,
    courseId = courseId,
    desc = desc,
    envelopePic = envelopePic,
    fresh = fresh,
    id = id,
    link = link,
    niceDate = niceDate,
    niceShareDate = niceShareDate,
    origin = origin,
    originId = originId,
    prefix = prefix,
    projectLink = projectLink,
    publishTime = publishTime,
    selfVisible = selfVisible,
    shareDate = shareDate,
    shareUser = shareUser,
    superChapterId = superChapterId,
    superChapterName = superChapterName,
    tags = tags,
    title = title,
    type = type,
    userId = userId,
    visible = visible,
    zan = zan,
    top = top,
    readTime = readTime,
)

fun Article.toStoredArticle() = StoredArticle(
    apkLink = apkLink,
    audit = audit,
    author = author,
    chapterId = chapterId,
    chapterName = chapterName,
    collect = collect,
    courseId = courseId,
    desc = desc,
    envelopePic = envelopePic,
    fresh = fresh,
    id = id,
    link = link,
    niceDate = niceDate,
    niceShareDate = niceShareDate,
    origin = origin,
    originId = originId,
    prefix = prefix,
    projectLink = projectLink,
    publishTime = publishTime,
    selfVisible = selfVisible,
    shareDate = shareDate,
    shareUser = shareUser,
    superChapterId = superChapterId,
    superChapterName = superChapterName,
    tags = tags,
    title = title,
    type = type,
    userId = userId,
    visible = visible,
    zan = zan,
    top = top,
    readTime = readTime,
)
