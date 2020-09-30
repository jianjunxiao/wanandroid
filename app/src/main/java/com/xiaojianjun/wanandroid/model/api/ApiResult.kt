package com.xiaojianjun.wanandroid.model.api

import androidx.annotation.Keep

/**
 * Created by xiaojianjun on 2019-09-18.
 */
@Keep
data class ApiResult<T>(val errorCode: Int, val errorMsg: String, private val data: T) {
    fun apiData(): T {
        if (errorCode == 0) {
            return data
        } else {
            throw ApiException(errorCode, errorMsg)
        }
    }
}
