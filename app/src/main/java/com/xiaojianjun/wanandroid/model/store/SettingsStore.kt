package com.xiaojianjun.wanandroid.model.store

import android.content.Context
import com.xiaojianjun.wanandroid.App
import com.xiaojianjun.wanandroid.common.core.getSpValue
import com.xiaojianjun.wanandroid.common.core.putSpValue

/**
 * Created by xiaojianjun on 2019-12-09.
 */
object SettingsStore {

    private const val SP_SETTINGS = "sp_settings"
    private const val DEFAULT_WEB_TEXT_ZOOM = 100
    private const val KEY_WEB_TEXT_ZOOM = "key_web_text_zoom"
    private const val KEY_NIGHT_MODE = "key_night_mode"

    fun setWebTextZoom(textZoom: Int) =
        putSpValue(SP_SETTINGS, App.instance, KEY_WEB_TEXT_ZOOM, textZoom)

    fun getWebTextZoom() =
        getSpValue(SP_SETTINGS, App.instance, KEY_WEB_TEXT_ZOOM, DEFAULT_WEB_TEXT_ZOOM)

    fun setNightMode(nightMode: Boolean) {
        App.instance
            .getSharedPreferences(SP_SETTINGS, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_NIGHT_MODE, nightMode)
            .commit()
    }

    fun getNightMode() =
        getSpValue(SP_SETTINGS, App.instance, KEY_NIGHT_MODE, false)
}
