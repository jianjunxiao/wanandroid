package com.xiaojianjun.wanandroid.common.bus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus

/**
 * Created by xiaojianjun on 2019-11-25.
 */
object Bus {

    /**
     * 发布LiveDataEventBus消息
     */
    inline fun <reified T> post(channel: String, value: T) {
        LiveEventBus.get(channel, T::class.java).post(value)
    }

    /**
     * 订阅LiveDataEventBus消息
     * @param channel 渠道
     * @param owner 生命周期owner
     * @param observer 观察者
     */
    inline fun <reified T> observe(channel: String, owner: LifecycleOwner, observer: Observer<T>) {
        LiveEventBus.get(channel, T::class.java).observe(owner, observer)
    }

    /**
     * 应用进程生命周期内订阅LiveDataEventBus消息
     * @param channel 渠道
     * @param observer 观察者
     */
    inline fun <reified T> observeForever(channel: String, observer: Observer<T>) {
        LiveEventBus.get(channel, T::class.java).observeForever(observer)
    }

    /**
     * 订阅粘性LiveDataEventBus消息
     * @param channel 渠道
     * @param owner 生命周期owner
     * @param observer 观察者
     */
    inline fun <reified T> observeSticky(
        channel: String,
        owner: LifecycleOwner,
        observer: Observer<T>
    ) {
        LiveEventBus.get(channel, T::class.java).observeSticky(owner, observer)
    }

    /**
     * 应用进程生命周期内订阅粘性LiveDataEventBus消息
     * @param channel 渠道
     * @param observer 观察者
     */
    inline fun <reified T> observeStickyForever(
        channel: String,
        observer: Observer<T>
    ) {
        LiveEventBus.get(channel, T::class.java).observeStickyForever(observer)
    }
}
