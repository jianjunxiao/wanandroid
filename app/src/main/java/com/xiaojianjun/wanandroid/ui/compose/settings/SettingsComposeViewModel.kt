package com.xiaojianjun.wanandroid.ui.compose.settings

import android.content.Context
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.xiaojianjun.wanandroid.model.api.RetrofitClient
import com.xiaojianjun.wanandroid.model.store.SettingsStore
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.ui.compose.common.ComposeBaseViewModel
import com.xiaojianjun.wanandroid.util.clearCache
import com.xiaojianjun.wanandroid.util.getCacheSize
import com.xiaojianjun.wanandroid.util.setNightMode
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

    fun refresh(context: Context) {
        _uiState.update {
            SettingsUiState(
                nightMode = SettingsStore.getNightMode(),
                webTextZoom = SettingsStore.getWebTextZoom(),
                cacheSize = getCacheSize(context),
                isLogin = isLogin(),
            )
        }
    }

    fun setNightModeEnabled(enabled: Boolean) {
        SettingsStore.setNightMode(enabled)
        setNightMode(enabled)
        _uiState.update { it.copy(nightMode = enabled) }
    }

    fun setWebTextZoom(textZoom: Int) {
        SettingsStore.setWebTextZoom(textZoom)
        _uiState.update { it.copy(webTextZoom = textZoom) }
    }

    fun clearCacheAndRefresh(context: Context) {
        clearCache(context)
        _uiState.update { it.copy(cacheSize = getCacheSize(context)) }
    }

    fun logout() {
        UserInfoStore.clearUserInfo()
        RetrofitClient.clearCookie()
        Bus.post(USER_LOGIN_STATE_CHANGED, false)
        _uiState.update { it.copy(isLogin = false) }
    }
}
