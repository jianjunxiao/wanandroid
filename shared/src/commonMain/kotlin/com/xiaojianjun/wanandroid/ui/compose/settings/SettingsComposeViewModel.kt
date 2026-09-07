package com.xiaojianjun.wanandroid.ui.compose.settings

import com.xiaojianjun.wanandroid.common.bus.AppEvents
import com.xiaojianjun.wanandroid.model.api.WanApiClient
import com.xiaojianjun.wanandroid.model.store.SettingsStore
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.platform.Platform
import com.xiaojianjun.wanandroid.ui.compose.common.ComposeBaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SettingsUiState(
    val nightMode: Boolean = false,
    val webTextZoom: Int = 100,
    val cacheSize: String = "",
    val isLogin: Boolean = false,
)

class SettingsComposeViewModel : ComposeBaseViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    fun refresh() = launchFlow(block = {
        val cacheSize = formatCacheSize(Platform.cacheSize())
        _uiState.update {
            SettingsUiState(
                nightMode = SettingsStore.getNightMode(),
                webTextZoom = SettingsStore.getWebTextZoom(),
                cacheSize = cacheSize,
                isLogin = isLogin(),
            )
        }
    })

    fun setNightModeEnabled(enabled: Boolean) {
        SettingsStore.setNightMode(enabled)
        _uiState.update { it.copy(nightMode = enabled) }
    }

    fun setWebTextZoom(textZoom: Int) {
        SettingsStore.setWebTextZoom(textZoom)
        _uiState.update { it.copy(webTextZoom = textZoom) }
    }

    fun clearCacheAndRefresh() = launchFlow(block = {
        Platform.clearCache()
        val cacheSize = formatCacheSize(Platform.cacheSize())
        _uiState.update { it.copy(cacheSize = cacheSize) }
    })

    fun logout(onComplete: () -> Unit) = launchFlow(block = {
        WanApiClient.clearCookie()
        UserInfoStore.clearUserInfo()
        AppEvents.loginChanged()
        _uiState.update { it.copy(isLogin = false) }
        onComplete()
    })
}

private fun formatCacheSize(bytes: Long): String {
    if (bytes < 1024) return "${bytes}B"
    val units = listOf("KB", "MB", "GB", "TB")
    var amount = bytes.toDouble() / 1024
    var index = 0
    while (amount >= 1024 && index < units.lastIndex) {
        amount /= 1024
        index++
    }
    val rounded = kotlin.math.round(amount * 100).toLong()
    return "${rounded / 100}.${(rounded % 100).toString().padStart(2, '0')}${units[index]}"
}
