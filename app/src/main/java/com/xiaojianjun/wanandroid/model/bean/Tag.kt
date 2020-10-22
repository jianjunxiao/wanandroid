package com.xiaojianjun.wanandroid.model.bean

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.Entity
import kotlinx.android.parcel.Parcelize

/**
 * Created by xiaojianjun on 2019-11-07.
 */
@Keep
@Parcelize
@Entity(primaryKeys = ["articleId", "name", "url"])
data class Tag(
    var articleId: Long = 0,
    var name: String = "",
    var url: String = ""
) : Parcelable