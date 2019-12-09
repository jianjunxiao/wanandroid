package com.xiaojianjun.wanandroid.ui.detail

import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.room.RoomHelper

/**
 * Created by xiaojianjun on 2019-12-05.
 */
class DetailRepository {
    suspend fun saveReadHistory(article: Article) = RoomHelper.addReadHistory(article)
}