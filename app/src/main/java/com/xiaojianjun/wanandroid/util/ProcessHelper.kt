package com.xiaojianjun.wanandroid.util

import android.app.ActivityManager
import android.content.Context
import android.os.Process

/**
 * 是否是主进程
 */
fun isMainProcess(context: Context) = context.packageName == currentProcessName(context)

/**
 * 当前进程的名称
 */
private fun currentProcessName(context: Context): String {
    val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
    for (process in manager.runningAppProcesses) {
        if (process.pid == Process.myPid()) {
            return process.processName
        }
    }
    return ""
}