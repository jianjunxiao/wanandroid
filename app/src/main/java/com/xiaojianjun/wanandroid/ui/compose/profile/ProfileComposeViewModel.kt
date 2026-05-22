package com.xiaojianjun.wanandroid.ui.compose.profile

import com.xiaojianjun.wanandroid.model.bean.UserInfo
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.ui.compose.common.ComposeBaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ProfileUiState(
    val isLogin: Boolean = false,
    val userInfo: UserInfo? = null,
)

class ProfileComposeViewModel : ComposeBaseViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        refreshLoginState()
    }

    fun refreshLoginState() {
        _uiState.update {
            ProfileUiState(
                isLogin = isLogin(),
                userInfo = UserInfoStore.getUserInfo(),
            )
        }
    }
}
