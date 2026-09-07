package com.xiaojianjun.wanandroid.ui.compose.login

import com.xiaojianjun.wanandroid.common.bus.AppEvents
import com.xiaojianjun.wanandroid.model.repository.LoginRepository
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.ui.compose.common.ComposeBaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LoginUiState(
    val account: String = "",
    val password: String = "",
    val accountError: String? = null,
    val passwordError: String? = null,
    val isSubmitting: Boolean = false,
)

sealed interface LoginEvent {
    data object LoginSuccess : LoginEvent
}

class LoginComposeViewModel(
    private val loginRepository: LoginRepository,
) : ComposeBaseViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _loginEvents = MutableSharedFlow<LoginEvent>()
    val loginEvents = _loginEvents.asSharedFlow()

    fun onAccountChanged(value: String) {
        _uiState.update { it.copy(account = value, accountError = null) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null) }
    }

    fun submit() {
        val state = _uiState.value
        when {
            state.account.isEmpty() -> {
                _uiState.update { it.copy(accountError = "账号不能为空") }
                return
            }
            state.password.isEmpty() -> {
                _uiState.update { it.copy(passwordError = "密码不能为空") }
                return
            }
        }

        _uiState.update { it.copy(isSubmitting = true) }
        launchFlow(
            block = {
                val userInfo = loginRepository.login(state.account, state.password)
                UserInfoStore.setUserInfo(userInfo)
                AppEvents.loginChanged()
                _uiState.update { it.copy(isSubmitting = false) }
                _loginEvents.emit(LoginEvent.LoginSuccess)
            },
            error = {
                _uiState.update { it.copy(isSubmitting = false) }
            },
        )
    }
}
