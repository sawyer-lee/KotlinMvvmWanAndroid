package com.sawyer.kotlinmvvmwanandroid.model.store

import com.google.gson.Gson
import com.sawyer.kotlinmvvmwanandroid.App
import com.sawyer.kotlinmvvmwanandroid.model.bean.UserInfo
import com.sawyer.kotlinmvvmwanandroid.util.clearSpValue
import com.sawyer.kotlinmvvmwanandroid.util.getSpValue
import com.sawyer.kotlinmvvmwanandroid.util.putSpValue

object UserInfoStore {
    private const val SP_USER_INFO = "sp_user_info"
    private const val KEY_USER_INFO = "userInfo"
    private val mGson by lazy { Gson() }

  /*  fun isLogin():Boolean{
        val userInfoStr= getSpValue(SP_USER_INFO, App.instance, KEY_USER_INFO,"")
        return userInfoStr.isNotEmpty()
    }*/

    /**设置用户信息、保存本地sp*/
    fun setUserInfo(userInfo: UserInfo){
        putSpValue(SP_USER_INFO,App.instance, KEY_USER_INFO, mGson.toJson(userInfo))
    }

    /**获取本地sp存储的用户信息*/
    fun getUserInfo(): UserInfo? {
        val userInfoStr = getSpValue(SP_USER_INFO, App.instance, KEY_USER_INFO, "")
        return if (userInfoStr.isNotEmpty()) {
            mGson.fromJson(userInfoStr, UserInfo::class.java)
        } else {
            null
        }
    }

    /**清除用户信息*/
    fun clearUserInfo() {
        clearSpValue(SP_USER_INFO, App.instance)
    }

    /**取消指定ID收藏*/
    fun removeCollectId(collectId: Long) {
        getUserInfo()?.let {
            if (collectId in it.collectIds) {
                it.collectIds.remove(collectId)
                setUserInfo(it)
            }
        }
    }

    /**添加指定ID收藏*/
    fun addCollectId(collectId: Long) {
        getUserInfo()?.let {
            if (collectId !in it.collectIds) {
                it.collectIds.add(collectId)
                setUserInfo(it)
            }
        }
    }
}