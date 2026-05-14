package com.xiaojianjun.wanandroid.common.bus

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.ConcurrentHashMap

/**
 * Created by xiaojianjun on 2019-11-25.
 */
object Bus {

    @PublishedApi
    internal val channels = ConcurrentHashMap<String, EventLiveData<Any>>()

    @PublishedApi
    internal fun channel(name: String): EventLiveData<Any> {
        return channels.getOrPut(name) { EventLiveData() }
    }

    /**
     * 发布LiveDataEventBus消息
     */
    inline fun <reified T> post(channel: String, value: T) {
        this.channel(channel).postEvent(value as Any)
    }

    /**
     * 订阅LiveDataEventBus消息
     * @param channel 渠道
     * @param owner 生命周期owner
     * @param observer 观察者
     */
    inline fun <reified T> observe(channel: String, owner: LifecycleOwner, observer: Observer<T>) {
        this.channel(channel).observeEvent(owner, observer, false)
    }

    /**
     * 应用进程生命周期内订阅LiveDataEventBus消息
     * @param channel 渠道
     * @param observer 观察者
     */
    inline fun <reified T> observeForever(channel: String, observer: Observer<T>) {
        this.channel(channel).observeEventForever(observer, false)
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
        this.channel(channel).observeEvent(owner, observer, true)
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
        this.channel(channel).observeEventForever(observer, true)
    }

    @PublishedApi
    internal class EventLiveData<T : Any> : MutableLiveData<T>() {
        private var version = 0

        @PublishedApi
        internal
        fun postEvent(value: T) {
            version++
            postValue(value)
        }

        @PublishedApi
        internal
        fun <R> observeEvent(owner: LifecycleOwner, observer: Observer<R>, sticky: Boolean) {
            val startVersion = if (sticky) -1 else version
            super.observe(owner, VersionedObserver(startVersion, observer))
        }

        @PublishedApi
        internal
        fun <R> observeEventForever(observer: Observer<R>, sticky: Boolean) {
            val startVersion = if (sticky) -1 else version
            super.observeForever(VersionedObserver(startVersion, observer))
        }

        private inner class VersionedObserver<R>(
            private var lastVersion: Int,
            private val observer: Observer<R>
        ) : Observer<T> {
            override fun onChanged(value: T) {
                if (lastVersion >= version) return
                lastVersion = version
                @Suppress("UNCHECKED_CAST")
                observer.onChanged(value as R)
            }
        }
    }
}
