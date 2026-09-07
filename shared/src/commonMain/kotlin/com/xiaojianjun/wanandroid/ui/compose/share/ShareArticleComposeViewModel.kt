package com.xiaojianjun.wanandroid.ui.compose.share

import com.xiaojianjun.wanandroid.model.repository.ShareRepository
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.resources.*
import com.xiaojianjun.wanandroid.ui.compose.common.ComposeBaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.StringResource

data class ShareArticleUiState(
    val sharePeople: String = "",
    val submitting: Boolean = false,
)

sealed interface ShareArticleEvent {
    data class Toast(val resId: StringResource) : ShareArticleEvent
}

class ShareArticleComposeViewModel(
    private val repository: ShareRepository,
) : ComposeBaseViewModel() {
    private val _uiState = MutableStateFlow(ShareArticleUiState())
    val uiState = _uiState.asStateFlow()

    private val _shareEvents = MutableSharedFlow<ShareArticleEvent>()
    val shareEvents = _shareEvents.asSharedFlow()

    init {
        val userInfo = UserInfoStore.getUserInfo()
        val sharePeople = when {
            userInfo == null -> ""
            userInfo.nickname.isNotEmpty() -> userInfo.nickname
            else -> userInfo.username
        }
        _uiState.update { it.copy(sharePeople = sharePeople) }
    }

    fun shareArticle(title: String, link: String) {
        launchFlow(
            block = {
                _uiState.update { it.copy(submitting = true) }
                repository.shareArticle(title, link)
                _uiState.update { it.copy(submitting = false) }
                _shareEvents.emit(ShareArticleEvent.Toast(Res.string.share_article_success))
            },
            error = {
                _uiState.update { it.copy(submitting = false) }
            },
        )
    }
}
