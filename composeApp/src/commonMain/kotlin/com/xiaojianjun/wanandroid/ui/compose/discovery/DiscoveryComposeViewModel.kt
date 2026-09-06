package com.xiaojianjun.wanandroid.ui.compose.discovery

import com.xiaojianjun.wanandroid.model.bean.Banner
import com.xiaojianjun.wanandroid.model.bean.Frequently
import com.xiaojianjun.wanandroid.model.bean.HotWord
import com.xiaojianjun.wanandroid.ui.compose.common.ComposeBaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DiscoveryUiState(
    val banners: List<Banner> = emptyList(),
    val hotWords: List<HotWord> = emptyList(),
    val frequentlyList: List<Frequently> = emptyList(),
    val isRefreshing: Boolean = false,
    val showReload: Boolean = false,
)

class DiscoveryComposeViewModel : ComposeBaseViewModel() {
    private val repository by lazy { DiscoveryRepository() }

    private val _uiState = MutableStateFlow(DiscoveryUiState())
    val uiState: StateFlow<DiscoveryUiState> = _uiState.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        launchFlow(
            block = {
                _uiState.update { it.copy(isRefreshing = true, showReload = false) }
                val banners = repository.getBanners()
                val hotWords = repository.getHotWords()
                val frequentlyList = repository.getFrequentlyWebsites()
                _uiState.update {
                    it.copy(
                        banners = banners,
                        hotWords = hotWords,
                        frequentlyList = frequentlyList,
                        isRefreshing = false,
                        showReload = false,
                    )
                }
            },
            error = {
                _uiState.update {
                    it.copy(
                        isRefreshing = false,
                        showReload = it.banners.isEmpty() && it.hotWords.isEmpty() && it.frequentlyList.isEmpty(),
                    )
                }
            },
        )
    }
}
