package com.xiaojianjun.wanandroid.model.store

import com.xiaojianjun.wanandroid.App
import com.xiaojianjun.wanandroid.util.core.getSpValue
import com.xiaojianjun.wanandroid.util.core.putSpValue

/**
 * Created by xiaojianjun on 2019-12-09.
 */
object SettingsStore {

    private const val SP_SETTINGS = "sp_settings"
    private const val KEY_WEB_TEXT_ZOOM = "key_web_text_zoom"
    private const val DEFAULT_WEB_TEXT_ZOOM = 100

    fun setWebTextZoom(textZoom: Int) {
        putSpValue(SP_SETTINGS, App.instance, KEY_WEB_TEXT_ZOOM, textZoom)
    }

    fun getWebTextZoom() =
        getSpValue(SP_SETTINGS, App.instance, KEY_WEB_TEXT_ZOOM, DEFAULT_WEB_TEXT_ZOOM)
}