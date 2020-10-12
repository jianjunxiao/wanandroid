package com.xiaojianjun.wanandroid.common.core

import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * moshi json 解析
 */
object MoshiHelper {

    /**json解析moshi对象**/
    val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    /**
     *  json string 解析成对象，可能为null
     *  @param [json] json string
     */
    inline fun <reified T> fromJson(json: String): T? {
        return moshi.adapter(T::class.java).fromJson(json)
    }

    /**
     * 将对象转换为json string
     * @param [t] 实体
     */
    inline fun <reified T> toJson(t: T): String {
        return moshi.adapter(T::class.java).toJson(t)
    }

    /**
     * 将json list string转换为对象列表
     * @param [json] json list string
     */
    inline fun <reified T> listfromJson(json: String): List<T> {
        val type = Types.newParameterizedType(List::class.java, T::class.java)
        return moshi.adapter<List<T>>(type).fromJson(json) ?: emptyList()
    }
}