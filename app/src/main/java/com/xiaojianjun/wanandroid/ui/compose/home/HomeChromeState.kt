package com.xiaojianjun.wanandroid.ui.compose.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class HomeChromeState {
    var bottomBarVisible by mutableStateOf(true)
        private set

    var toolbarOffset by mutableIntStateOf(0)
        private set

    private var currentOffset = 0

    fun onToolbarOffsetChanged(offset: Int) {
        if (currentOffset == offset) return
        bottomBarVisible = offset > currentOffset
        currentOffset = offset
        toolbarOffset = offset
    }
}
