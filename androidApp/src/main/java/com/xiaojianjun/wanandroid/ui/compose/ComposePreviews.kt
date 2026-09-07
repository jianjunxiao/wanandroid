package com.xiaojianjun.wanandroid.ui.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.ui.compose.home.PopularContent
import com.xiaojianjun.wanandroid.ui.compose.home.PopularUiState
import com.xiaojianjun.wanandroid.ui.compose.login.LoginContent
import com.xiaojianjun.wanandroid.ui.compose.login.LoginUiState
import com.xiaojianjun.wanandroid.ui.compose.theme.WanAndroidTheme

@Preview(showBackground = true)
@Composable
private fun PopularContentPreview() {
    WanAndroidTheme {
        PopularContent(
            state = PopularUiState(
                articles = listOf(
                    Article(
                        id = 1L,
                        author = "xuexuexue",
                        superChapterName = "项目基础功能",
                        chapterName = "完整项目",
                        title = "随心音乐，让心跟着跳动起来",
                        desc = "一款或许可以激励你写代码的 IntelliJ IDEA 插件。",
                        niceDate = "2019-10-20",
                        top = true,
                    )
                )
            ),
            onReload = {},
            onLoadMore = {},
            onArticleClick = {},
            onCollectClick = {},
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginContentPreview() {
    WanAndroidTheme {
        LoginContent(
            state = LoginUiState(),
            onBack = {},
            onAccountChanged = {},
            onPasswordChanged = {},
            onSubmit = {},
            onRegister = {},
        )
    }
}
