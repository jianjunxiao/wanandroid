package com.xiaojianjun.wanandroid.ui.compose.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.JsonEncodingException
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.model.api.ApiException
import com.xiaojianjun.wanandroid.model.api.RetrofitClient
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

sealed interface ComposeUiEvent {
    data class Toast(val message: String) : ComposeUiEvent
    data class ToastResource(val resId: Int) : ComposeUiEvent
    data object LoginInvalid : ComposeUiEvent
}

open class ComposeBaseViewModel : ViewModel() {
    private val _events = MutableSharedFlow<ComposeUiEvent>()
    val events = _events.asSharedFlow()

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

    protected suspend fun sendToast(message: String) {
        _events.emit(ComposeUiEvent.Toast(message))
    }

    protected suspend fun sendToast(messageRes: Int) {
        _events.emit(ComposeUiEvent.ToastResource(messageRes))
    }

    private suspend fun handleError(e: Exception, showError: Boolean) {
        when (e) {
            is ApiException -> {
                when (e.code) {
                    -1001 -> {
                        UserInfoStore.clearUserInfo()
                        RetrofitClient.clearCookie()
                        _events.emit(ComposeUiEvent.LoginInvalid)
                    }
                    else -> if (showError) _events.emit(ComposeUiEvent.Toast(e.message))
                }
            }
            is ConnectException,
            is SocketTimeoutException,
            is UnknownHostException,
            is HttpException,
            is SSLHandshakeException -> if (showError) {
                _events.emit(ComposeUiEvent.ToastResource(R.string.network_request_failed))
            }
            is JsonDataException,
            is JsonEncodingException -> if (showError) {
                _events.emit(ComposeUiEvent.ToastResource(R.string.api_data_parse_error))
            }
            else -> if (showError) {
                _events.emit(ComposeUiEvent.Toast(e.message.orEmpty()))
            }
        }
    }
}
