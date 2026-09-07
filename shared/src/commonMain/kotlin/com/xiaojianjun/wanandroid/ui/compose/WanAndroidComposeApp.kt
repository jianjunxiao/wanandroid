package com.xiaojianjun.wanandroid.ui.compose

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.Scene
import androidx.navigation3.ui.NavDisplay
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.crossfade
import coil3.svg.SvgDecoder
import com.xiaojianjun.wanandroid.common.core.ImageCache
import com.xiaojianjun.wanandroid.di.AppContainer
import com.xiaojianjun.wanandroid.di.LocalAppContainer
import com.xiaojianjun.wanandroid.model.api.WanApiClient
import com.xiaojianjun.wanandroid.model.store.SettingsStore
import com.xiaojianjun.wanandroid.platform.PlatformBackHandler
import com.xiaojianjun.wanandroid.platform.rememberPlatformViewModelDecorator
import com.xiaojianjun.wanandroid.resources.*
import com.xiaojianjun.wanandroid.ui.compose.common.UiMessageHost
import com.xiaojianjun.wanandroid.ui.compose.common.UiMessages
import com.xiaojianjun.wanandroid.ui.compose.detail.ArticleDetailScreen
import com.xiaojianjun.wanandroid.ui.compose.discovery.DiscoveryScreen
import com.xiaojianjun.wanandroid.ui.compose.home.HomeChromeState
import com.xiaojianjun.wanandroid.ui.compose.home.HomeScreen
import com.xiaojianjun.wanandroid.ui.compose.login.LoginScreen
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanNavigationState
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanNavigator
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute
import com.xiaojianjun.wanandroid.ui.compose.navigationpage.NavigationScreen
import com.xiaojianjun.wanandroid.ui.compose.opensource.OpenSourceScreen
import com.xiaojianjun.wanandroid.ui.compose.personal.CollectionScreen
import com.xiaojianjun.wanandroid.ui.compose.personal.HistoryScreen
import com.xiaojianjun.wanandroid.ui.compose.personal.SharedArticlesScreen
import com.xiaojianjun.wanandroid.ui.compose.points.MinePointsScreen
import com.xiaojianjun.wanandroid.ui.compose.points.PointsRankScreen
import com.xiaojianjun.wanandroid.ui.compose.profile.ProfileScreen
import com.xiaojianjun.wanandroid.ui.compose.register.RegisterScreen
import com.xiaojianjun.wanandroid.ui.compose.search.SearchScreen
import com.xiaojianjun.wanandroid.ui.compose.settings.SettingsScreen
import com.xiaojianjun.wanandroid.ui.compose.share.ShareArticleScreen
import com.xiaojianjun.wanandroid.ui.compose.system.SystemScreen
import com.xiaojianjun.wanandroid.ui.compose.systembars.ComposeSystemBarsEffect
import com.xiaojianjun.wanandroid.ui.compose.theme.WanAndroidTheme
import com.xiaojianjun.wanandroid.ui.compose.theme.WanTheme
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private val TopLevelRoutes = listOf(
    TopLevelItem(WanRoute.Home, Res.string.home, Res.drawable.ic_home_black_24dp),
    TopLevelItem(WanRoute.System, Res.string.system, Res.drawable.ic_equalizer_black_24dp),
    TopLevelItem(WanRoute.Discovery, Res.string.discovery, Res.drawable.ic_layers_black_24dp),
    TopLevelItem(WanRoute.Navigation, Res.string.navigation, Res.drawable.ic_navigation_black_24dp),
    TopLevelItem(WanRoute.Profile, Res.string.mine, Res.drawable.ic_person_black_24dp),
)

private data class TopLevelItem(
    val route: WanRoute,
    val labelRes: StringResource,
    val iconRes: DrawableResource,
)

private const val TopLevelRouteMetadataKey = "topLevelRoute"
private val TopLevelRouteMetadata = mapOf(TopLevelRouteMetadataKey to true)

@Composable
fun WanAndroidComposeApp(
    modifier: Modifier = Modifier,
    onExit: () -> Unit = {},
    container: AppContainer = remember { AppContainer() },
) {
    CompositionLocalProvider(LocalAppContainer provides container) {
        WanAndroidContent(modifier = modifier, onExit = onExit)
    }
}

@OptIn(androidx.compose.animation.ExperimentalSharedTransitionApi::class)
@Composable
private fun WanAndroidContent(
    modifier: Modifier = Modifier,
    onExit: () -> Unit = {},
) {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .crossfade(200)
            .components {
                add(KtorNetworkFetcherFactory(httpClient = { WanApiClient.client }))
                add(SvgDecoder.Factory())
            }
            .build().also { ImageCache.loader = it }
    }
    var darkTheme by remember { mutableStateOf(SettingsStore.getNightMode()) }

    WanAndroidTheme(darkTheme = darkTheme) {
        ComposeSystemBarsEffect(darkTheme = darkTheme)
        var navigationVersion by remember { mutableIntStateOf(0) }
        var homeReselectVersion by remember { mutableIntStateOf(0) }
        var navigationReselectVersion by remember { mutableIntStateOf(0) }
        val homeChromeState = remember { HomeChromeState() }
        val systemChromeState = remember { HomeChromeState() }
        val discoveryChromeState = remember { HomeChromeState() }
        val navigationChromeState = remember { HomeChromeState() }
        val navigationState = remember {
            WanNavigationState(
                startRoute = WanRoute.Home,
                topLevelRoutes = TopLevelRoutes.map { it.route }.toSet(),
            )
        }
        val navigator = remember(navigationState) {
            WanNavigator(navigationState) { navigationVersion++ }
        }
        val backStack = remember(navigationVersion) { navigator.backStack }
        LaunchedEffect(navigator) {
            UiMessages.loginRequests.collect {
                if (navigator.backStack.lastOrNull() != WanRoute.Login) navigator.navigate(WanRoute.Login)
            }
        }
        val currentRoute = backStack.lastOrNull()
        val isTopLevelRoute = TopLevelRoutes.any { it.route == currentRoute }
        val showBottomBar = isTopLevelRoute && when (currentRoute) {
            WanRoute.Home -> homeChromeState.bottomBarVisible
            WanRoute.System -> systemChromeState.bottomBarVisible
            WanRoute.Discovery -> discoveryChromeState.bottomBarVisible
            WanRoute.Navigation -> navigationChromeState.bottomBarVisible
            else -> true
        }

        PlatformBackHandler {
            if (!navigator.goBack()) onExit()
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .background(WanTheme.colors.backgroundSecondary),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding(),
            ) {
                NavDisplay(
                    backStack = backStack,
                    onBack = { navigator.goBack() },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberPlatformViewModelDecorator(),
                    ),
                    entryProvider = entryProvider {
                        entry<WanRoute.Home>(metadata = TopLevelRouteMetadata) {
                            HomeScreen(
                                navigator = navigator,
                                chromeState = homeChromeState,
                                reselectVersion = homeReselectVersion,
                            )
                        }
                        entry<WanRoute.System>(metadata = TopLevelRouteMetadata) {
                            SystemScreen(
                                navigator = navigator,
                                chromeState = systemChromeState,
                            )
                        }
                        entry<WanRoute.Discovery>(metadata = TopLevelRouteMetadata) {
                            DiscoveryScreen(
                                navigator = navigator,
                                chromeState = discoveryChromeState,
                            )
                        }
                        entry<WanRoute.Navigation>(metadata = TopLevelRouteMetadata) {
                            NavigationScreen(
                                navigator = navigator,
                                chromeState = navigationChromeState,
                                reselectVersion = navigationReselectVersion,
                            )
                        }
                        entry<WanRoute.Profile>(metadata = TopLevelRouteMetadata) {
                            ProfileScreen(navigator = navigator)
                        }
                        entry<WanRoute.Login> {
                            LoginScreen(navigator = navigator)
                        }
                        entry<WanRoute.Register> {
                            RegisterScreen(navigator = navigator)
                        }
                        entry<WanRoute.Settings> {
                            SettingsScreen(
                                navigator = navigator,
                                onNightModeChanged = { darkTheme = it },
                            )
                        }
                        entry<WanRoute.OpenSource> {
                            OpenSourceScreen(navigator = navigator)
                        }
                        entry<WanRoute.ShareArticle> {
                            ShareArticleScreen(navigator = navigator)
                        }
                        entry<WanRoute.PointsRank> {
                            PointsRankScreen(navigator = navigator)
                        }
                        entry<WanRoute.MinePoints> {
                            MinePointsScreen(navigator = navigator)
                        }
                        entry<WanRoute.SharedArticles> {
                            SharedArticlesScreen(navigator = navigator)
                        }
                        entry<WanRoute.Collection> {
                            CollectionScreen(navigator = navigator)
                        }
                        entry<WanRoute.History> {
                            HistoryScreen(navigator = navigator)
                        }
                        entry<WanRoute.Search> { route ->
                            SearchScreen(
                                navigator = navigator,
                                initialKeywords = route.initialKeywords,
                            )
                        }
                        entry<WanRoute.ArticleDetail> { route ->
                            ArticleDetailScreen(route = route, navigator = navigator)
                        }
                    },
                    transitionSpec = {
                        if (isTopLevelRouteTransition()) {
                            topLevelRouteTransform()
                        } else {
                            forwardRouteTransform()
                        }
                    },
                    popTransitionSpec = {
                        if (isTopLevelRouteTransition()) {
                            topLevelRouteTransform()
                        } else {
                            popRouteTransform()
                        }
                    },
                    predictivePopTransitionSpec = {
                        if (isTopLevelRouteTransition()) {
                            topLevelRouteTransform()
                        } else {
                            popRouteTransform()
                        }
                    },
                )

                WanBottomNavigation(
                    visible = showBottomBar,
                    currentRoute = navigator.currentTopLevelRoute,
                    onItemClick = { route ->
                        if (route == navigator.currentTopLevelRoute) {
                            if (route == WanRoute.Home) homeReselectVersion++
                            if (route == WanRoute.Navigation) navigationReselectVersion++
                        } else {
                            navigator.switchTopLevel(route)
                        }
                    },
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
            }
            StatusBarTitlePlaceholder(
                modifier = Modifier.align(Alignment.TopCenter),
            )
            UiMessageHost(Modifier.align(Alignment.BottomCenter).padding(bottom = 64.dp))
            NavigationBarTabPlaceholder(
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }
}

@Composable
private fun StatusBarTitlePlaceholder(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsTopHeight(WindowInsets.statusBars)
            .background(WanTheme.colors.backgroundPrimary),
    )
}

@Composable
private fun NavigationBarTabPlaceholder(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsBottomHeight(WindowInsets.navigationBars)
            .background(WanTheme.colors.backgroundPrimary),
    )
}

private fun androidx.compose.animation.AnimatedContentTransitionScope<Scene<WanRoute>>.isTopLevelRouteTransition(): Boolean {
    return initialState.isTopLevelRouteScene() && targetState.isTopLevelRouteScene()
}

private fun Scene<WanRoute>.isTopLevelRouteScene(): Boolean {
    return metadata[TopLevelRouteMetadataKey] == true
}

private fun topLevelRouteTransform(): ContentTransform {
    return fadeIn(
        animationSpec = tween(140, easing = FastOutSlowInEasing),
    ) togetherWith fadeOut(
        animationSpec = tween(100, easing = FastOutLinearInEasing),
    )
}

private fun forwardRouteTransform(): ContentTransform {
    return slideInHorizontally(
        initialOffsetX = { it },
        animationSpec = tween(280, easing = LinearOutSlowInEasing),
    ) togetherWith slideOutHorizontally(
        targetOffsetX = { -it / 4 },
        animationSpec = tween(220, easing = FastOutLinearInEasing),
    )
}

private fun popRouteTransform(): ContentTransform {
    return slideInHorizontally(
        initialOffsetX = { -it / 4 },
        animationSpec = tween(280, easing = LinearOutSlowInEasing),
    ) togetherWith slideOutHorizontally(
        targetOffsetX = { it },
        animationSpec = tween(240, easing = FastOutLinearInEasing),
    )
}

@Composable
private fun WanBottomNavigation(
    visible: Boolean,
    currentRoute: WanRoute?,
    onItemClick: (WanRoute) -> Unit,
    modifier: Modifier = Modifier,
) {
    val barHeight = 56.dp
    val shadowHeight = 4.dp
    val offsetY by animateDpAsState(
        targetValue = if (visible) 0.dp else barHeight + shadowHeight,
        animationSpec = tween(
            durationMillis = if (visible) 225 else 175,
            easing = FastOutSlowInEasing,
        ),
        label = "LegacyBottomNavigationOffset",
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(barHeight + shadowHeight)
            .clipToBounds(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = offsetY)
                .height(barHeight + shadowHeight),
        ) {
            MainTabTopShadow()
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(barHeight)
                    .background(WanTheme.colors.backgroundPrimary),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TopLevelRoutes.forEach { item ->
                    val selected = currentRoute == item.route
                    val contentColor = if (selected) WanTheme.colors.textPrimary else WanTheme.colors.textThird
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clickable { onItemClick(item.route) },
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Icon(
                            painter = painterResource(item.iconRes),
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier.size(24.dp),
                        )
                        Box(modifier = Modifier.height(2.dp))
                        Text(
                            text = stringResource(item.labelRes),
                            color = contentColor,
                            fontSize = 12.sp,
                            lineHeight = 12.sp,
                        )
                        Box(modifier = Modifier.height(1.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun MainTabTopShadow(
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(4.dp),
    ) {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    Color.Black.copy(alpha = 0.02f),
                    Color.Black.copy(alpha = 0.05f),
                ),
            ),
        )
    }
}
