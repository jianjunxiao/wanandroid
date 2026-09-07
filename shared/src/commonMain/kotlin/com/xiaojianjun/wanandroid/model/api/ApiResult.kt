package com.xiaojianjun.wanandroid.model.api

import kotlinx.serialization.Serializable

/**
 *
 * Created by xiaojianjun on 2019-09-18.
 */

@Serializable
data class ApiResult<T>(
    val errorCode: Int,
    val errorMsg: String,
    private val data: T?
) {
    fun checkError() {
        if (errorCode != 0) throw ApiException(errorCode, errorMsg)
    }

    fun apiData(): T {
        if (errorCode == 0 && data != null) {
            return data
        } else {
            throw ApiException(errorCode, errorMsg)
        }
    }
}
