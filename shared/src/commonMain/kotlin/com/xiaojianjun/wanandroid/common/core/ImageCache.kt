package com.xiaojianjun.wanandroid.common.core

import coil3.ImageLoader

object ImageCache {
    var loader: ImageLoader? = null
    fun size(): Long = (loader?.memoryCache?.size ?: 0L) + (loader?.diskCache?.size ?: 0L)
    fun clear() {
        loader?.memoryCache?.clear()
        loader?.diskCache?.clear()
    }
}
