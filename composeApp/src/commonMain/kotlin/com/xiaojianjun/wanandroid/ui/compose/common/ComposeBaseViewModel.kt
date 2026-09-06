package com.xiaojianjun.wanandroid.ui.compose.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.xiaojianjun.wanandroid.common.bus.AppEvents
import com.xiaojianjun.wanandroid.model.api.ApiException
import com.xiaojianjun.wanandroid.model.api.WanApiClient
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.resources.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import org.jetbrains.compose.resources.StringResource

open class ComposeBaseViewModel : ViewModel() {
    protected fun launchFlow(
        block: suspend CoroutineScope.() -> Unit,
        error: (suspend (Exception) -> Unit)? = null,
        showError: Boolean = true,
    ): Job {
        return viewModelScope.launch {
            try {
                block()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                handleError(e, showError)
                error?.invoke(e)
            }
        }
    }

    protected fun sendToast(message: String) = UiMessages.showToast(message)

    protected fun sendToast(messageRes: StringResource) = UiMessages.showToast(messageRes)

    private suspend fun handleError(e: Exception, showError: Boolean) {
        when (e) {
            is ApiException -> {
                when (e.code) {
                    -1001 -> {
                        UserInfoStore.clearUserInfo()
                        try {
                            WanApiClient.clearCookie()
                        } catch (cleanupError: Exception) {
                            if (cleanupError is CancellationException) throw cleanupError
                            // 会话已失效；代理清理失败不能阻止本地退出和登录提示。
                        }
                        AppEvents.loginChanged()
                        UiMessages.requireLogin()
                    }
                    else -> if (showError) sendToast(e.message)
                }
            }
            is SerializationException -> if (showError) {
                sendToast(Res.string.api_data_parse_error)
            }
            else -> if (showError) {
                sendToast(Res.string.network_request_failed)
            }
        }
    }
}
