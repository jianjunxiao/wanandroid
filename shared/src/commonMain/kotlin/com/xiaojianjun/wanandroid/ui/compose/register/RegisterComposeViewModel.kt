package com.xiaojianjun.wanandroid.ui.compose.register

import com.xiaojianjun.wanandroid.common.bus.AppEvents
import com.xiaojianjun.wanandroid.model.repository.RegisterRepository
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.resources.*
import com.xiaojianjun.wanandroid.ui.compose.common.ComposeBaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.StringResource

data class RegisterUiState(
    val account: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val accountError: StringResource? = null,
    val passwordError: StringResource? = null,
    val confirmPasswordError: StringResource? = null,
    val isSubmitting: Boolean = false,
)

sealed interface RegisterEvent {
    data object RegisterSuccess : RegisterEvent
}

class RegisterComposeViewModel(
    private val repository: RegisterRepository,
) : ComposeBaseViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState = _uiState.asStateFlow()

    private val _registerEvents = MutableSharedFlow<RegisterEvent>()
    val registerEvents = _registerEvents.asSharedFlow()

    fun onAccountChanged(value: String) {
        _uiState.update { it.copy(account = value.take(20), accountError = null) }
    }

    fun onPasswordChanged(value: String) {
        _uiState.update { it.copy(password = value.take(20), passwordError = null, confirmPasswordError = null) }
    }

    fun onConfirmPasswordChanged(value: String) {
        _uiState.update { it.copy(confirmPassword = value.take(20), confirmPasswordError = null) }
    }

    fun submit() {
        val state = _uiState.value
        when {
            state.account.isEmpty() -> {
                _uiState.update { it.copy(accountError = Res.string.account_can_not_be_empty) }
                return
            }
            state.account.length < 3 -> {
                _uiState.update { it.copy(accountError = Res.string.account_length_over_three) }
                return
            }
            state.password.isEmpty() -> {
                _uiState.update { it.copy(passwordError = Res.string.password_can_not_be_empty) }
                return
            }
            state.password.length < 6 -> {
                _uiState.update { it.copy(passwordError = Res.string.password_length_over_six) }
                return
            }
            state.confirmPassword.isEmpty() -> {
                _uiState.update { it.copy(confirmPasswordError = Res.string.confirm_password_can_not_be_empty) }
                return
            }
            state.password != state.confirmPassword -> {
                _uiState.update { it.copy(confirmPasswordError = Res.string.two_password_are_inconsistent) }
                return
            }
        }

        _uiState.update { it.copy(isSubmitting = true) }
        launchFlow(
            block = {
                val userInfo = repository.register(state.account, state.password, state.confirmPassword)
                UserInfoStore.setUserInfo(userInfo)
                AppEvents.loginChanged()
                _uiState.update { it.copy(isSubmitting = false) }
                _registerEvents.emit(RegisterEvent.RegisterSuccess)
            },
            error = {
                _uiState.update { it.copy(isSubmitting = false) }
            },
        )
    }
}
