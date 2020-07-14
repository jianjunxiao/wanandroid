package com.xiaojianjun.wanandroid.model.api

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.xiaojianjun.wanandroid.App
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by xiaojianjun on 2019-09-18.
 * Retrofit网络请求工具
 */
object RetrofitClient {
    /**Cookie*/
    private val cookiePersistor = SharedPrefsCookiePersistor(App.instance)
    private val cookieJar = PersistentCookieJar(SetCookieCache(), cookiePersistor)

    /**OkhttpClient*/
    private val okHttpClient = OkHttpClient.Builder()
        .callTimeout(10, TimeUnit.SECONDS)
        .cookieJar(cookieJar)
        .build()

    /**Retrofit*/
    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(ApiService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**ApiService*/
    val apiService: ApiService = retrofit.create(ApiService::class.java)

    /**清除Cookie*/
    fun clearCookie() = cookieJar.clear()

    /**是否有Cookie*/
    fun hasCookie() = cookiePersistor.loadAll().isNotEmpty()
}