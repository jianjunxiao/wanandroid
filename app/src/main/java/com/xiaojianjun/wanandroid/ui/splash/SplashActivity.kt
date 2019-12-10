package com.xiaojianjun.wanandroid.ui.splash

import android.os.Bundle
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.model.store.SettingsStore
import com.xiaojianjun.wanandroid.ui.base.BaseActivity
import com.xiaojianjun.wanandroid.ui.main.MainActivity
import com.xiaojianjun.wanandroid.util.core.ActivityManager
import com.xiaojianjun.wanandroid.util.setNightMode

class SplashActivity : BaseActivity() {

    override fun layoutRes() = R.layout.activity_splash

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.postDelayed({
            ActivityManager.start(MainActivity::class.java)
            ActivityManager.finish(SplashActivity::class.java)
        }, 1000)
    }
}
