package com.xiaojianjun.wanandroid.model.room

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Created by xiaojianjun on 2019-12-05.
 */
@Database(entities = [StoredArticle::class, StoredTag::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun readHistoryDao(): ReadHistoryDao
}
