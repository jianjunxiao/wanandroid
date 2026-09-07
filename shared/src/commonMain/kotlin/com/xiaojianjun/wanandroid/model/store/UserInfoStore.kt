package com.xiaojianjun.wanandroid.model.store

import com.xiaojianjun.wanandroid.common.core.JsonCodec
import com.xiaojianjun.wanandroid.model.bean.UserInfo
import com.xiaojianjun.wanandroid.platform.PlatformPreferences

/**
 * Created by xiaojianjun on 2019-11-24.
 * 用户信息存储
 */
object UserInfoStore {

    private const val SP_USER_INFO = "sp_user_info"
    private const val KEY_USER_INFO = "userInfo"

    /**
     * 获取本地sp存储的用户信息
     */
    fun getUserInfo(): UserInfo? {
        val userInfoStr = PlatformPreferences.getString(SP_USER_INFO, KEY_USER_INFO, "")
        return if (userInfoStr.isNotEmpty()) {
            JsonCodec.fromJson<UserInfo>(userInfoStr)
        } else {
            null
        }
    }

    /**
     * 设置用户信息、保存本地sp
     */
    fun setUserInfo(userInfo: UserInfo) =
        PlatformPreferences.putString(SP_USER_INFO, KEY_USER_INFO, JsonCodec.toJson(userInfo))

    /**
     * 清除用户信息
     */
    fun clearUserInfo() {
        PlatformPreferences.clear(SP_USER_INFO)
    }

    fun removeCollectId(collectId: Long) {
        getUserInfo()?.let {
            if (collectId in it.collectIds) {
                it.collectIds.remove(collectId)
                setUserInfo(it)
            }
        }
    }

    fun addCollectId(collectId: Long) {
        getUserInfo()?.let {
            if (collectId !in it.collectIds) {
                it.collectIds.add(collectId)
                setUserInfo(it)
            }
        }
    }
}
