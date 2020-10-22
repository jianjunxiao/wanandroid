package com.xiaojianjun.wanandroid

import android.app.Application
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.core.CoilHelper
import com.xiaojianjun.wanandroid.common.core.DayNightHelper
import com.xiaojianjun.wanandroid.common.loadmore.LoadMoreHelper
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
        LoadMoreHelper.init()
        CoilHelper.init(this)
        ActivityHelper.init(this)
        DayNightHelper.init()
    }

}