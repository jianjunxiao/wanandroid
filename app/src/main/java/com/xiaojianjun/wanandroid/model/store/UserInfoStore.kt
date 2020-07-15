package com.xiaojianjun.wanandroid.model.store

import com.google.gson.Gson
import com.xiaojianjun.wanandroid.App
import com.xiaojianjun.wanandroid.model.bean.UserInfo
import com.xiaojianjun.wanandroid.common.core.clearSpValue
import com.xiaojianjun.wanandroid.common.core.getSpValue
import com.xiaojianjun.wanandroid.common.core.putSpValue

/**
 * Created by xiaojianjun on 2019-11-24.
 * 用户信息存储
 */
object UserInfoStore {

    private const val SP_USER_INFO = "sp_user_info"
    private const val KEY_USER_INFO = "userInfo"
    private val mGson by lazy { Gson() }

    /**
     * 获取本地sp存储的用户信息
     */
    fun getUserInfo(): UserInfo? {
        val userInfoStr = getSpValue(SP_USER_INFO, App.instance, KEY_USER_INFO, "")
        return if (userInfoStr.isNotEmpty()) {
            mGson.fromJson(userInfoStr, UserInfo::class.java)
        } else {
            null
        }
    }

    /**
     * 设置用户信息、保存本地sp
     */
    fun setUserInfo(userInfo: UserInfo) =
        putSpValue(SP_USER_INFO, App.instance, KEY_USER_INFO, mGson.toJson(userInfo))

    /**
     * 清除用户信息
     */
    fun clearUserInfo() {
        clearSpValue(SP_USER_INFO, App.instance)
    }

    fun removeCollectId(collectId: Int) {
        getUserInfo()?.let {
            if (collectId in it.collectIds) {
                it.collectIds.remove(collectId)
                setUserInfo(it)
            }
        }
    }

    fun addCollectId(collectId: Int) {
        getUserInfo()?.let {
            if (collectId !in it.collectIds) {
                it.collectIds.add(collectId)
                setUserInfo(it)
            }
        }
    }
}