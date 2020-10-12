package com.xiaojianjun.wanandroid.model.room

import androidx.room.*
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.bean.Tag

/**
 * Created by xiaojianjun on 2019-12-05.
 */
@Dao
interface ReadHistoryDao {
    @Transaction
    @Insert(entity = Article::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article): Long

    @Transaction
    @Insert(entity = Tag::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: Tag): Long

    @Transaction
    @Query("SELECT * FROM article ORDER BY readTime DESC")
    suspend fun queryAllReadHistory(): List<ReadHistory>

    @Transaction
    @Query("SELECT * FROM article WHERE id = (:id)")
    suspend fun queryReadHistory(id: Long): ReadHistory?

    @Transaction
    @Delete(entity = Article::class)
    suspend fun deleteArticle(article: Article)

    @Transaction
    @Delete(entity = Tag::class)
    suspend fun deleteTag(tag: Tag)

}