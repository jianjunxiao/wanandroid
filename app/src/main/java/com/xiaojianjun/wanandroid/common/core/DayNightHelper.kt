package com.xiaojianjun.wanandroid.common.core

import com.xiaojianjun.wanandroid.model.store.SettingsStore
import com.xiaojianjun.wanandroid.util.setNightMode

object DayNightHelper {
    @JvmStatic
    fun init() {
        setNightMode(SettingsStore.getNightMode())
    }
}