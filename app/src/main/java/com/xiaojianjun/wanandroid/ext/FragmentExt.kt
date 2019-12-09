package com.xiaojianjun.wanandroid.ext

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

/**
 * Created by xiaojianjun on 2019-11-21.
 */

fun Fragment.openInExplorer(link: String?) {
    startActivity(Intent().apply {
        action = Intent.ACTION_VIEW
        data = Uri.parse(link)
    })
}