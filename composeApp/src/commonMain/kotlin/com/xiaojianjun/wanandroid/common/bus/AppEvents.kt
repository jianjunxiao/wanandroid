package com.xiaojianjun.wanandroid.common.bus

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object AppEvents {
    private val loginVersion = MutableStateFlow(0)
    val loginChanges = loginVersion.asStateFlow()
    private val collections = MutableSharedFlow<Pair<Long, Boolean>>(extraBufferCapacity = 32)
    val collectionChanges = collections.asSharedFlow()

    fun loginChanged() = loginVersion.update { it + 1 }
    fun collectionChanged(change: Pair<Long, Boolean>) { collections.tryEmit(change) }
}
