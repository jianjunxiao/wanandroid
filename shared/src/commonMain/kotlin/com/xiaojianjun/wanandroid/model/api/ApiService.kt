package com.xiaojianjun.wanandroid.model.api

import com.xiaojianjun.wanandroid.common.core.JsonCodec
import com.xiaojianjun.wanandroid.model.bean.*
import com.xiaojianjun.wanandroid.platform.Platform
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.*
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.statement.HttpResponse
import io.ktor.http.*
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

class ApiService(private val client: HttpClient) {
    companion object {
        const val BASE_URL = "https://wanandroid.com"
    }

    private suspend inline fun <reified T> decodeResponse(response: HttpResponse): ApiResult<T> =
        withContext(Platform.ioDispatcher) {
            // 接口统一返回 UTF-8；直接解码字节，避免原生字符集转换阻塞 UI。
            JsonCodec.json.decodeFromString<ApiResult<T>>(
                response.body<ByteArray>().decodeToString()
            ).also { it.checkError() }
        }

    private suspend inline fun <reified T> get(path: String): ApiResult<T> =
        decodeResponse(client.get(Platform.apiBaseUrl + "/" + path))

    private suspend inline fun <reified T> post(
        path: String,
        vararg fields: Pair<String, String>,
    ): ApiResult<T> = decodeResponse(
        client.post(Platform.apiBaseUrl + "/" + path) {
            setBody(FormDataContent(Parameters.build {
                fields.forEach { (key, value) -> append(key, value) }
            }))
        }
    )

    private suspend fun action(path: String, vararg fields: Pair<String, String>): ApiResult<JsonElement> {
        val result = post<JsonElement>(path, *fields)
        // WanAndroid 的写接口成功时 data 为 null，成功状态不依赖响应体数据。
        return ApiResult(result.errorCode, result.errorMsg, JsonNull)
    }

    suspend fun getProjectList(page: Int) = get<Pagination<Article>>("article/listproject/$page/json")
    suspend fun getTopArticleList() = get<List<Article>>("article/top/json")
    suspend fun getArticleList(page: Int) = get<Pagination<Article>>("article/list/$page/json")
    suspend fun getUserArticleList(page: Int) = get<Pagination<Article>>("user_article/list/$page/json")
    suspend fun getArticleCategories() = get<MutableList<Category>>("tree/json")
    suspend fun getArticleListByCid(page: Int, cid: Int) = get<Pagination<Article>>("article/list/$page/json?cid=$cid")
    suspend fun getProjectCategories() = get<MutableList<Category>>("project/tree/json")
    suspend fun getProjectListByCid(page: Int, cid: Int) = get<Pagination<Article>>("project/list/$page/json?cid=$cid")
    suspend fun getWechatCategories() = get<MutableList<Category>>("wxarticle/chapters/json")
    suspend fun getWechatArticleList(page: Int, id: Int) = get<Pagination<Article>>("wxarticle/list/$id/$page/json")
    suspend fun getNavigations() = get<List<Navigation>>("navi/json")
    suspend fun getBanners() = get<List<Banner>>("banner/json")
    suspend fun getHotWords() = get<List<HotWord>>("hotkey/json")
    suspend fun getFrequentlyWebsites() = get<List<Frequently>>("friend/json")
    suspend fun login(username: String, password: String) = post<UserInfo>("user/login", "username" to username, "password" to password)
    suspend fun register(username: String, password: String, repassword: String) = post<UserInfo>("user/register", "username" to username, "password" to password, "repassword" to repassword)
    suspend fun collect(id: Long) = action("lg/collect/$id/json")
    suspend fun uncollect(id: Long) = action("lg/uncollect_originId/$id/json")
    suspend fun search(keywords: String, page: Int) = post<Pagination<Article>>("article/query/$page/json", "k" to keywords)
    suspend fun shareArticle(title: String, link: String) = action("lg/user_article/add/json", "title" to title, "link" to link)
    suspend fun getPoints() = get<PointRank>("lg/coin/userinfo/json")
    suspend fun getPointsRecord(page: Int) = get<Pagination<PointRecord>>("lg/coin/list/$page/json")
    suspend fun getPointsRank(page: Int) = get<Pagination<PointRank>>("coin/rank/$page/json")
    suspend fun getCollectionList(page: Int) = get<Pagination<Article>>("lg/collect/list/$page/json")
    suspend fun getSharedArticleList(page: Int) = get<Shared>("user/lg/private_articles/$page/json")
    suspend fun deleteShare(id: Long) = action("lg/user_article/delete/$id/json")
}
