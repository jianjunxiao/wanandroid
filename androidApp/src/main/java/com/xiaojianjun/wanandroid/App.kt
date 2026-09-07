package com.xiaojianjun.wanandroid

import android.app.Application
import com.xiaojianjun.wanandroid.model.room.RoomHelper
import com.xiaojianjun.wanandroid.model.store.ReadHistoryStore
import com.xiaojianjun.wanandroid.platform.AndroidPlatform
import com.xiaojianjun.wanandroid.util.isMainProcess

/**
 * Created by xiaojianjun on 2019-07-15.
 */
class App : Application() {

    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        // 主进程初始化
        if (isMainProcess(this)) {
            init()
        }
    }

    private fun init() {
        AndroidPlatform.initialize(this, BuildConfig.VERSION_NAME)
        ReadHistoryStore.storage = RoomHelper
    }

}
