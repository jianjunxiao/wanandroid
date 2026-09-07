package com.xiaojianjun.wanandroid.di

import com.xiaojianjun.wanandroid.model.api.ApiService
import com.xiaojianjun.wanandroid.model.api.WanApiClient
import com.xiaojianjun.wanandroid.model.repository.*
import com.xiaojianjun.wanandroid.model.store.ReadHistoryStorage
import com.xiaojianjun.wanandroid.model.store.ReadHistoryStore
import com.xiaojianjun.wanandroid.model.store.SearchHistoryStorage
import com.xiaojianjun.wanandroid.model.store.SearchHistoryStore

/** 应用级数据依赖在这里组装，ViewModel 只接收其需要的 Repository。 */
class AppContainer(
    apiService: ApiService = WanApiClient.apiService,
    readHistory: ReadHistoryStorage = ReadHistoryStore,
    searchHistory: SearchHistoryStorage = SearchHistoryStore,
) {
    val collectRepository = CollectRepository(apiService)
    val discoveryRepository = DiscoveryRepository(apiService)
    val latestRepository = LatestRepository(apiService)
    val plazaRepository = PlazaRepository(apiService)
    val popularRepository = PopularRepository(apiService)
    val projectRepository = ProjectRepository(apiService)
    val wechatRepository = WechatRepository(apiService)
    val loginRepository = LoginRepository(apiService)
    val navigationRepository = NavigationRepository(apiService)
    val collectionRepository = CollectionRepository(apiService)
    val historyRepository = HistoryRepository(readHistory)
    val sharedRepository = SharedRepository(apiService)
    val minePointsRepository = MinePointsRepository(apiService)
    val pointsRankRepository = PointsRankRepository(apiService)
    val registerRepository = RegisterRepository(apiService)
    val searchHistoryRepository = SearchHistoryRepository(apiService, searchHistory)
    val searchResultRepository = SearchResultRepository(apiService)
    val shareRepository = ShareRepository(apiService)
    val systemPagerRepository = SystemPagerRepository(apiService)
    val systemRepository = SystemRepository(apiService)
}
