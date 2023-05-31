package com.sawyer.kotlinmvvmwanandroid.ui.main.mine

import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient
import com.sawyer.kotlinmvvmwanandroid.model.store.UserInfoStore
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseViewModel
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.Bus
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_LOGIN_STATE_CHANGED

class MineViewModel : BaseViewModel() {

    fun logout() {
        UserInfoStore.clearUserInfo()
        RetrofitClient.clearCookie()
        Bus.post(USER_LOGIN_STATE_CHANGED, false)
    }
}