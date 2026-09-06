package com.xiaojianjun.wanandroid.common.core

import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json

object JsonCodec {
    val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
        isLenient = true
    }

    inline fun <reified T> toJson(value: T): String = json.encodeToString(value)

    inline fun <reified T> fromJson(value: String): T? = try {
        json.decodeFromString<T>(value)
    } catch (_: SerializationException) {
        null
    } catch (_: IllegalArgumentException) {
        null
    }
}
