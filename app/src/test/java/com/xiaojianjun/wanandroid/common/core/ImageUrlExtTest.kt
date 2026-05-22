package com.xiaojianjun.wanandroid.common.core

import com.xiaojianjun.wanandroid.model.api.ApiService
import org.junit.Assert.assertEquals
import org.junit.Test

class ImageUrlExtTest {

    @Test
    fun normalizeWanAndroidImageUrlReplacesWwwBaseUrl() {
        val url = "https://www.wanandroid.com/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png"

        assertEquals(
            "${ApiService.BASE_URL}/blogimgs/50c115c2-cf6c-4802-aa7b-a4334de444cd.png",
            url.normalizeWanAndroidImageUrl(),
        )
    }

    @Test
    fun normalizeWanAndroidImageUrlKeepsOtherUrls() {
        val url = "https://wanandroid.com/blogimgs/demo.png"

        assertEquals(url, url.normalizeWanAndroidImageUrl())
    }
}
