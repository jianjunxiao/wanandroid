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
     *  @param [type] 解析的泛型类型。注意：如果解析的目标实体类有泛型，type不能为空，否则会报错；如果解析目标实
     *                体类没有泛型，type可以不传。
     */
    inline fun <reified T> fromJson(json: String, type: Type? = null): T? {
        return if (type == null) {
            moshi.adapter(T::class.java).fromJson(json)
        } else {
            moshi.adapter<T>(type).fromJson(json)
        }
    }

    /**
     * 将对象转换为json string
     * @param [t] 实体
     */
    inline fun <reified T> toJson(t: T): String {
        return moshi.adapter(T::class.java).toJson(t)
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