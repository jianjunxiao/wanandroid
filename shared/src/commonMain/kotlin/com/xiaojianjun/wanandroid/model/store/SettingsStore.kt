package com.xiaojianjun.wanandroid.model.store

import com.xiaojianjun.wanandroid.platform.PlatformPreferences

/**
 * Created by xiaojianjun on 2019-12-09.
 */
object SettingsStore {

    private const val SP_SETTINGS = "sp_settings"
    private const val DEFAULT_WEB_TEXT_ZOOM = 100
    private const val KEY_WEB_TEXT_ZOOM = "key_web_text_zoom"
    private const val KEY_NIGHT_MODE = "key_night_mode"

    fun setWebTextZoom(textZoom: Int) =
        PlatformPreferences.putInt(SP_SETTINGS, KEY_WEB_TEXT_ZOOM, textZoom)

    fun getWebTextZoom() =
        PlatformPreferences.getInt(SP_SETTINGS, KEY_WEB_TEXT_ZOOM, DEFAULT_WEB_TEXT_ZOOM)

    fun setNightMode(nightMode: Boolean) {
        PlatformPreferences.putBoolean(SP_SETTINGS, KEY_NIGHT_MODE, nightMode)
    }

    fun getNightMode() =
        PlatformPreferences.getBoolean(SP_SETTINGS, KEY_NIGHT_MODE, false)
}
