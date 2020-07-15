package com.xiaojianjun.wanandroid.util

import android.content.Context
import android.os.Environment.MEDIA_MOUNTED
import android.os.Environment.getExternalStorageState
import java.io.File
import java.math.BigDecimal
import java.math.BigDecimal.ROUND_HALF_UP

/**
 * Created by xiaojianjun on 2019-12-09.
 */

/**
 * 获取缓存大小，包含内部内部缓存和外部缓存
 */
fun getCacheSize(context: Context): String {
    var cacheSize = getFolderSize(context.cacheDir)
    if (getExternalStorageState() == MEDIA_MOUNTED) {
        cacheSize += getFolderSize(context.externalCacheDir)
    }
    return getFormateSize(cacheSize)
}

fun getFormateSize(cacheSize: Long): String {
    val kb = cacheSize / 1024.0
    if (kb < 1) {
        return "${BigDecimal(cacheSize).setScale(2, ROUND_HALF_UP).toPlainString()}B"
    }
    val mb = kb / 1024.0
    if (mb < 1) {
        return "${BigDecimal(kb).setScale(2, ROUND_HALF_UP).toPlainString()}KB"
    }
    val gb = mb / 1024.0
    if (gb < 1) {
        return "${BigDecimal(mb).setScale(2, ROUND_HALF_UP).toPlainString()}MB"
    }
    val tb = gb / 1024.0
    if (tb < 1) {
        return "${BigDecimal(gb).setScale(2, ROUND_HALF_UP).toPlainString()}GB"
    }
    return "${BigDecimal(tb).setScale(2, ROUND_HALF_UP).toPlainString()}TB"
}

private fun getFolderSize(file: File?): Long {
    var size = 0L
    file?.listFiles()?.forEach {
        size += if (it.isDirectory) getFolderSize(it) else it.length()
    }
    return size
}

/**
 * 清除缓存
 */
fun clearCache(context: Context) {
    deleteDir(context.cacheDir)
    if (getExternalStorageState() == MEDIA_MOUNTED) {
        deleteDir(context.externalCacheDir)
    }
}

private fun deleteDir(file: File?): Boolean {
    if (file == null) return false
    if (file.isDirectory) {
        file.list()?.forEach {
            val success = deleteDir(File(file, it))
            if (!success) return false
        }
    }
    return file.delete()
}
