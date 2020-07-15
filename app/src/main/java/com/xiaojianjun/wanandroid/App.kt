package com.xiaojianjun.wanandroid

import android.app.Application
import com.xiaojianjun.wanandroid.model.store.SettingsStore
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.util.isMainProcess
import com.xiaojianjun.wanandroid.util.setNightMode

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
        ActivityHelper.init(this)
        setDayNightMode()
    }

    private fun setDayNightMode() {
        setNightMode(SettingsStore.getNightMode())
    }
}