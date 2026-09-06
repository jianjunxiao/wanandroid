package com.xiaojianjun.wanandroid.model.room

import androidx.room.*

/**
 * Created by xiaojianjun on 2019-12-05.
 */
@Dao
interface ReadHistoryDao {
    @Transaction
    @Insert(entity = StoredArticle::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: StoredArticle): Long

    @Transaction
    @Insert(entity = StoredTag::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: StoredTag): Long

    @Transaction
    @Query("SELECT * FROM article ORDER BY readTime DESC")
    suspend fun queryAllReadHistory(): List<ReadHistory>

    @Transaction
    @Query("SELECT * FROM article WHERE id = :id")
    suspend fun queryReadHistory(id: Long): ReadHistory?

    @Transaction
    @Query("SELECT * FROM tag WHERE articleId = :articleId")
    suspend fun queryAllTags(articleId: Long): List<StoredTag>

    @Transaction
    @Delete(entity = StoredArticle::class)
    suspend fun deleteArticle(article: StoredArticle)

    @Transaction
    @Delete(entity = StoredTag::class)
    suspend fun deleteTag(tag: StoredTag)

}
