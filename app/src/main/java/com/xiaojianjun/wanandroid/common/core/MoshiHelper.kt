package com.xiaojianjun.wanandroid.common.core

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

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
        return moshi.adapter<T>(getType<T>()).fromJson(json)
    }

    /**
     * 将对象转换为json string
     * @param [t] 实体
     */
    inline fun <reified T> toJson(t: T): String {
        return moshi.adapter(T::class.java).toJson(t)
    }

    /**
     * 获取指定泛型T的Type
     */
    inline fun <reified T> getType(): Type {
        return (object : TypeToken<T>() {}).type
    }

    /**
     * 泛型类型获取，根据指定的泛型类型T，获取对应的ParameterizedType
     * 比如: (object : MoshiHelper.TypeToken<ApiResult<List<Map<String,Int>>>>() {}).type
     */
    abstract class TypeToken<T> {

        /**
         * 泛型T对应的Type
         */
        var type: Type

        init {
            val parameterizedType = this.javaClass.genericSuperclass
                    as ParameterizedType
            type = parameterizedType.actualTypeArguments[0]
        }
    }
}