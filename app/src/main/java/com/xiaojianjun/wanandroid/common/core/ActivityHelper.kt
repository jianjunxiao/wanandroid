package com.xiaojianjun.wanandroid.common.core

import android.app.Activity
import android.app.Application
import android.content.Intent
import com.xiaojianjun.wanandroid.common.simple.ActivityLifecycleCallbacksAdapter
import com.xiaojianjun.wanandroid.ext.putExtras

/**
 * Created by xiaojianjun on 2019-10-17.
 */
object ActivityHelper {

    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksAdapter(
            onActivityCreated = { activity, _ ->
                activities.add(activity)
            },
            onActivityDestroyed = { activity ->
                activities.remove(activity)
            }
        ))
    }

    val activities = mutableListOf<Activity>()

    fun start(clazz: Class<out Activity>, params: Map<String, Any> = emptyMap()) {
        val currentActivity = activities[activities.lastIndex]
        val intent = Intent(currentActivity, clazz)
        params.forEach {
            intent.putExtras(it.key to it.value)
        }
        currentActivity.startActivity(intent)
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
