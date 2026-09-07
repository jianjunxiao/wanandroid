package com.xiaojianjun.wanandroid.model.api

import com.xiaojianjun.wanandroid.common.core.JsonCodec
import com.xiaojianjun.wanandroid.model.bean.PointRank
import com.xiaojianjun.wanandroid.model.bean.UserInfo
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.*
import io.ktor.http.content.OutgoingContent
import kotlin.test.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.JsonNull

class ApiServiceTest {
    @Test
    fun successfulActionsAcceptNullData() = runTest {
        val paths = mutableListOf<String>()
        val client = HttpClient(MockEngine { request ->
            paths += request.url.encodedPath
            respond("""{"errorCode":0,"errorMsg":"","data":null}""", headers = headersOf(HttpHeaders.ContentType, "application/json"))
        })
        try {
            val service = ApiService(client)
            assertEquals(JsonNull, service.collect(12).apiData())
            assertEquals(JsonNull, service.uncollect(12).apiData())
            assertEquals(JsonNull, service.shareArticle("标题", "https://example.com/?a=1&b=2").apiData())
            assertEquals(JsonNull, service.deleteShare(13).apiData())
            assertEquals(listOf("/lg/collect/12/json", "/lg/uncollect_originId/12/json", "/lg/user_article/add/json", "/lg/user_article/delete/13/json"), paths)
        } finally { client.close() }
    }

    @Test
    fun loginEncodesChineseAndSpecialCharacters() = runTest {
        val client = HttpClient(MockEngine { request ->
            assertEquals(HttpMethod.Post, request.method)
            assertEquals("/user/login", request.url.encodedPath)
            val form = parseQueryString((request.body as OutgoingContent.ByteArrayContent).bytes().decodeToString())
            assertEquals("测试+账号&name", form["username"])
            assertEquals("p+a&ss= word", form["password"])
            respond("""{"errorCode":0,"errorMsg":"","data":{"admin":false,"email":"","icon":"","id":42,"nickname":"测试","password":"","publicName":"测试","token":"","type":0,"username":"测试+账号&name","collectIds":[12],"chapterTops":[],"newField":true}}""")
        })
        try {
            val user = ApiService(client).login("测试+账号&name", "p+a&ss= word").apiData()
            assertEquals(42, user.id)
            assertEquals(listOf(12L), user.collectIds)
        } finally { client.close() }
    }

    @Test
    fun expiredSessionPreservesServerError() = runTest {
        val client = HttpClient(MockEngine { respond("""{"errorCode":-1001,"errorMsg":"请先登录","data":null}""") })
        try {
            val error = assertFailsWith<ApiException> { ApiService(client).getCollectionList(0) }
            assertEquals(-1001, error.code)
            assertEquals("请先登录", error.message)
        } finally { client.close() }
    }

    @Test
    fun searchPreservesKeywordsAndPagination() = runTest {
        val client = HttpClient(MockEngine { request ->
            assertEquals("/article/query/2/json", request.url.encodedPath)
            val form = parseQueryString((request.body as OutgoingContent.ByteArrayContent).bytes().decodeToString())
            assertEquals("Compose 鸿蒙", form["k"])
            respond("""{"errorCode":0,"errorMsg":"","data":{"offset":40,"size":1,"total":41,"pageCount":3,"curPage":3,"over":true,"datas":[{"id":2147483648,"title":"Compose &amp; 鸿蒙","link":"https://example.com","tags":[]}]}}""")
        })
        try {
            val page = ApiService(client).search("Compose 鸿蒙", 2).apiData()
            assertTrue(page.over)
            assertEquals(3, page.curPage)
            assertEquals(2147483648L, page.datas.single().id)
        } finally { client.close() }
    }

    @Test
    fun largeUtf8ResponsePreservesChineseAndEmoji() = runTest {
        val title = "跨平台中文与表情 😀".repeat(2000)
        val payload = """{"errorCode":0,"errorMsg":"","data":[{"id":1,"title":"$title"}]}"""
        val client = HttpClient(MockEngine {
            respond(payload.encodeToByteArray(), headers = headersOf(HttpHeaders.ContentType, "application/json; charset=UTF-8"))
        })
        try {
            assertEquals(title, ApiService(client).getTopArticleList().apiData().single().title)
        } finally { client.close() }
    }

    @Test
    fun rankAcceptsNumericStrings() {
        val rank = JsonCodec.json.decodeFromString<PointRank>("""{"coinCount":12,"level":1,"rank":"7","userId":42,"username":"测试"}""")
        assertEquals(7, rank.rank)
    }

    @Test
    fun userDataRoundTripsAndRejectsMalformedJson() {
        val json = """{"admin":false,"email":"","icon":"","id":42,"nickname":"测试","password":"","publicName":"测试","token":"","type":0,"username":"qa","collectIds":[12,13],"chapterTops":[1,"Android"]}"""
        val user = assertNotNull(JsonCodec.fromJson<UserInfo>(json))
        assertEquals(user, JsonCodec.fromJson<UserInfo>(JsonCodec.toJson(user)))
        assertNull(JsonCodec.fromJson<UserInfo>("{broken"))
    }
}
