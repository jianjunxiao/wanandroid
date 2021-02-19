package com.xiaojianjun.wanandroid.common.core

import android.content.Intent
import android.content.IntentSender
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * 启动Activity，接口异步回调结果
 */
fun ActivityResultCaller.startActivityForResult(intent: Intent, callback: (ActivityResult) -> Unit) {
    var launcher: ActivityResultLauncher<Intent>? = null
    launcher = registerForActivityResult(StartActivityForResult()) {
        callback.invoke(it)
        launcher?.unregister()
    }
    launcher.launch(intent)
}

/**
 * 启动Activity，协程同步返回结果
 */
suspend fun ActivityResultCaller.startActivityForResult(intent: Intent): ActivityResult {
    return suspendCoroutine { continuation ->
        var launcher: ActivityResultLauncher<Intent>? = null
        launcher = registerForActivityResult(StartActivityForResult()) {
            continuation.resume(it)
            launcher?.unregister()
        }
        launcher.launch(intent)
    }
}

/**
 * 请求单个权限，接口异步回调请求结果
 */
fun ActivityResultCaller.requestPermission(
    permission: String,
    callback: (Boolean) -> Unit
) {
    var launcher: ActivityResultLauncher<String>? = null
    launcher =
        registerForActivityResult(RequestPermission()) { result ->
            callback.invoke(result)
            launcher?.unregister()
        }
    launcher.launch(permission)
}

/**
 * 请求单个权限，协程同步返回请求结果
 */
suspend fun ActivityResultCaller.requestPermission(
    permission: String
): Boolean {
    return suspendCoroutine { continuation ->
        var launcher: ActivityResultLauncher<String>? = null
        launcher =
            registerForActivityResult(RequestPermission()) {
                continuation.resume(it)
                launcher?.unregister()
            }
        launcher.launch(permission)
    }
}

/**
 * 请求多项权限，接口异步回调请求结果
 */
fun ActivityResultCaller.requestMultiplePermissions(
    vararg permissions: String,
    callback: (MutableMap<String, Boolean>) -> Unit
) {
    var launcher: ActivityResultLauncher<Array<out String>>? = null
    launcher =
        registerForActivityResult(RequestMultiplePermissions()) { result ->
            callback.invoke(result)
            launcher?.unregister()
        }
    launcher.launch(permissions)
}

/**
 * 请求多项权限，协程同步返回请求结果
 */
suspend fun ActivityResultCaller.requestMultiplePermissions(
    vararg permissions: String
): MutableMap<String, Boolean> {
    return suspendCoroutine { continuation ->
        var launcher: ActivityResultLauncher<Array<out String>>? = null
        launcher =
            registerForActivityResult(RequestMultiplePermissions()) { result ->
                continuation.resume(result)
                launcher?.unregister()
            }
        launcher.launch(permissions)
    }
}

/**
 * 启动intentSender，接口异步回调结果
 */
fun ActivityResultCaller.startIntentSenderForResult(
    intentSender: IntentSender,
    callback: (ActivityResult) -> Unit
) {
    var launcher: ActivityResultLauncher<IntentSenderRequest>? = null
    launcher = registerForActivityResult(StartIntentSenderForResult()) {
        callback.invoke(it)
        launcher?.unregister()
    }
    launcher.launch(
        IntentSenderRequest
            .Builder(intentSender)
            .build()
    )
}

/**
 * 启动intentSender，协程同步返回结果
 */
suspend fun ActivityResultCaller.startIntentSenderForResult(
    intentSender: IntentSender
): ActivityResult {
    return suspendCoroutine { continuation ->
        var launcher: ActivityResultLauncher<IntentSenderRequest>? = null
        launcher = registerForActivityResult(StartIntentSenderForResult()) {
            continuation.resume(it)
            launcher?.unregister()
        }
        launcher.launch(
            IntentSenderRequest
                .Builder(intentSender)
                .build()
        )
    }
}