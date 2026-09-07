package com.xiaojianjun.wanandroid.di

import androidx.lifecycle.ViewModelStore
import com.xiaojianjun.wanandroid.model.api.ApiService
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.store.ReadHistoryStorage
import com.xiaojianjun.wanandroid.model.store.SearchHistoryStorage
import com.xiaojianjun.wanandroid.ui.compose.detail.ArticleDetailComposeViewModel
import com.xiaojianjun.wanandroid.ui.compose.home.PopularComposeViewModel
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class AppContainerTest {
    @Test
    fun homeLoadsFromInjectedApiAndPreservesPinnedOrder() = runTest {
        val paths = mutableListOf<String>()
        val client = HttpClient(MockEngine { request ->
            paths += request.url.encodedPath
            val data = when (request.url.encodedPath) {
                "/article/top/json" -> """[{"id":1,"title":"置顶文章","link":"https://example.com/1"}]"""
                "/article/list/0/json" -> """{"offset":0,"size":1,"total":1,"pageCount":1,"curPage":1,"over":true,"datas":[{"id":2,"title":"普通文章","link":"https://example.com/2"}]}"""
                else -> error("出现未预期的请求路径")
            }
            respond("""{"errorCode":0,"errorMsg":"","data":$data}""")
        })
        val store = ViewModelStore()
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val container = AppContainer(ApiService(client), MemoryReadHistory(), MemorySearchHistory())
            val viewModel = PopularComposeViewModel(container.popularRepository, container.collectRepository)
            store.put("home", viewModel)
            val state = viewModel.uiState.first { !it.isRefreshing }
            assertEquals(listOf("/article/top/json", "/article/list/0/json"), paths)
            assertEquals(listOf(1L, 2L), state.articles.map { it.id })
            assertTrue(state.articles.first().top)
            assertFalse(state.articles.last().top)
            assertFalse(state.showReload)
        } finally {
            store.clear()
            Dispatchers.resetMain()
            client.close()
        }
    }

    @Test
    fun detailAndHistoryUseTheSameInjectedStorage() = runTest {
        val history = MemoryReadHistory()
        val client = HttpClient(MockEngine { error("阅读历史不应请求网络") })
        val store = ViewModelStore()
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        try {
            val container = AppContainer(ApiService(client), history, MemorySearchHistory())
            val viewModel = ArticleDetailComposeViewModel(container.historyRepository)
            store.put("detail", viewModel)
            viewModel.saveReadHistory(WanRoute.ArticleDetail(0, "无效文章", "https://example.com"))
            viewModel.saveReadHistory(WanRoute.ArticleDetail(3, "文章", "https://example.com/3"))
            advanceUntilIdle()
            val articles = container.historyRepository.getReadHistory()
            assertEquals(listOf(3L), articles.map { it.id })
            container.historyRepository.deleteHistory(articles.single())
            assertTrue(history.articles.isEmpty())
        } finally {
            store.clear()
            Dispatchers.resetMain()
            client.close()
        }
    }

    @Test
    fun searchHistoryCanRunWithoutPlatformPreferences() {
        val history = MemorySearchHistory()
        val client = HttpClient(MockEngine { error("本地搜索历史不应请求网络") })
        try {
            val container = AppContainer(ApiService(client), MemoryReadHistory(), history)
            container.searchHistoryRepository.saveSearchHistory("Compose")
            container.searchHistoryRepository.saveSearchHistory("Kotlin")
            assertEquals(listOf("Kotlin", "Compose"), container.searchHistoryRepository.getSearchHistory())
            container.searchHistoryRepository.deleteSearchHistory("Compose")
            assertEquals(listOf("Kotlin"), history.words)
        } finally {
            client.close()
        }
    }
}

private class MemoryReadHistory : ReadHistoryStorage {
    val articles = mutableListOf<Article>()
    override suspend fun queryAllReadHistory(): List<Article> = articles.toList()
    override suspend fun addReadHistory(article: Article) { articles.add(article) }
    override suspend fun deleteReadHistory(article: Article) { articles.removeAll { it.id == article.id } }
}

private class MemorySearchHistory : SearchHistoryStorage {
    val words = mutableListOf<String>()
    override fun saveSearchHistory(words: String) { this.words.add(0, words) }
    override fun deleteSearchHistory(words: String) { this.words.remove(words) }
    override fun getSearchHistory(): MutableList<String> = words.toMutableList()
}
