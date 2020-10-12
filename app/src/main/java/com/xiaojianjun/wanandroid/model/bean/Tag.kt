package com.xiaojianjun.wanandroid.model.bean

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

/**
 * Created by xiaojianjun on 2019-11-07.
 */
@Keep
@Parcelize
@Entity
data class Tag(
    @PrimaryKey(autoGenerate = true)
    var id: Long?,
    var articleId: Long = 0,
    var name: String?,
    var url: String?
) : Parcelable