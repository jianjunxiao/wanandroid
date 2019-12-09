package com.xiaojianjun.wanandroid.util

import android.content.Context
import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES

/**
 * Created by xiaojianjun on 2019-12-08.
 */
fun isNightMode(context: Context): Boolean {
    val mode = context.resources.configuration.uiMode and UI_MODE_NIGHT_MASK
    return mode == UI_MODE_NIGHT_YES
}

fun toggleDayNightMode(context: Context) {
    AppCompatDelegate.setDefaultNightMode(
        if (isNightMode(context)) MODE_NIGHT_NO else MODE_NIGHT_YES
    )
}
