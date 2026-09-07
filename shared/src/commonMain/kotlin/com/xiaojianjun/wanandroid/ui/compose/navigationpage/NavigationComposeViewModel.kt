package com.xiaojianjun.wanandroid.ui.compose.navigationpage

import com.xiaojianjun.wanandroid.model.bean.Navigation
import com.xiaojianjun.wanandroid.model.repository.NavigationRepository
import com.xiaojianjun.wanandroid.ui.compose.common.ComposeBaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class NavigationUiState(
    val navigations: List<Navigation> = emptyList(),
    val isRefreshing: Boolean = false,
    val showReload: Boolean = false,
)

class NavigationComposeViewModel(
    private val repository: NavigationRepository,
) : ComposeBaseViewModel() {
    private val _uiState = MutableStateFlow(NavigationUiState(isRefreshing = true))
    val uiState = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        launchFlow(
            block = {
                _uiState.update { it.copy(isRefreshing = true, showReload = false) }
                val navigations = repository.getNavigations()
                _uiState.update {
                    it.copy(
                        navigations = navigations,
                        isRefreshing = false,
                        showReload = false,
                    )
                }
            },
            error = {
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        showReload = it.navigations.isEmpty(),
                    )
                }
            },
        )
    }
}
