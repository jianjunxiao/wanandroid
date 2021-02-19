package com.xiaojianjun.wanandroid.common.core

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCaller
import com.xiaojianjun.wanandroid.common.simple.ActivityLifecycleCallbacksAdapter
import com.xiaojianjun.wanandroid.ext.putExtras
import com.xiaojianjun.wanandroid.ext.startActivityForResult
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Created by xiaojianjun on 2019-10-17.
 */
object ActivityHelper {

    private lateinit var applicationContext: Context

    fun init(application: Application) {
        applicationContext = application.applicationContext
        activities.clear()
        application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksAdapter(
            onActivityCreated = { activity, _ ->
                activities.add(activity)
            },
            onActivityDestroyed = { activity ->
                activities.remove(activity)
            }
        ))
    }

    private val activities = mutableListOf<Activity>()

    @JvmOverloads
    fun startActivity(
        clazz: Class<out Activity>,
        params: Map<String, Any> = emptyMap(),
    ) {
        val currentActivity = activities[activities.lastIndex]
        val intent = Intent(currentActivity, clazz)
        params.forEach {
            intent.putExtras(it.key to it.value)
        }
        currentActivity.startActivity(intent)
    }

    @JvmOverloads
    fun startActivityForResult(
        caller: ActivityResultCaller,
        clazz: Class<out Activity>,
        params: Map<String, Any> = emptyMap(),
        callback: (ActivityResult) -> Unit
    ) {
        val intent = Intent(applicationContext, clazz)
        params.forEach {
            intent.putExtras(it.key to it.value)
        }
        caller.startActivityForResult(intent) {
            callback.invoke(it)
        }
    }

    @JvmOverloads
    suspend fun startActivityForResult(
        caller: ActivityResultCaller,
        clazz: Class<out Activity>,
        params: Map<String, Any> = emptyMap(),
    ): ActivityResult {
        return suspendCoroutine { continuation ->
            startActivityForResult(caller, clazz, params) {
                continuation.resume(it)
            }
        }
    }

    /**
     * finish指定的一个或多个Activity
     */
    fun finish(vararg clazz: Class<out Activity>) {
        activities.forEach { activiy ->
            if (clazz.contains(activiy::class.java)) {
                activiy.finish()
            }
        }
    }

}