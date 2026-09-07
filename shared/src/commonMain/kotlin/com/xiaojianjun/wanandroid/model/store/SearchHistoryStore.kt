package com.xiaojianjun.wanandroid.model.store

import com.xiaojianjun.wanandroid.common.core.JsonCodec
import com.xiaojianjun.wanandroid.platform.PlatformPreferences

/**
 * Created by xiaojianjun on 2019-11-28.
 */
interface SearchHistoryStorage {
    fun saveSearchHistory(words: String)
    fun deleteSearchHistory(words: String)
    fun getSearchHistory(): MutableList<String>
}

object SearchHistoryStore : SearchHistoryStorage {

    private const val SP_SEARCH_HISTORY = "sp_search_history"
    private const val KEY_SEARCH_HISTORY = "searchHistory"

    override fun saveSearchHistory(words: String) {
        val history = getSearchHistory()
        if (history.contains(words)) {
            history.remove(words)
        }
        history.add(0, words)
        val listStr = JsonCodec.toJson(history)
        PlatformPreferences.putString(SP_SEARCH_HISTORY, KEY_SEARCH_HISTORY, listStr)
    }

    override fun deleteSearchHistory(words: String) {
        val history = getSearchHistory()
        history.remove(words)
        val listStr = JsonCodec.toJson(history)
        PlatformPreferences.putString(SP_SEARCH_HISTORY, KEY_SEARCH_HISTORY, listStr)
    }

    override fun getSearchHistory(): MutableList<String> {
        val listStr = PlatformPreferences.getString(SP_SEARCH_HISTORY, KEY_SEARCH_HISTORY, "")
        return if (listStr.isEmpty()) {
            mutableListOf()
        } else {
            JsonCodec.fromJson<MutableList<String>>(listStr) ?: mutableListOf()
        }
    }
}
