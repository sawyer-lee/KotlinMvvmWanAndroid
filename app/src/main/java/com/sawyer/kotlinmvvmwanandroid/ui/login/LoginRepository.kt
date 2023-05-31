package com.sawyer.kotlinmvvmwanandroid.ui.login

import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient
import com.sawyer.kotlinmvvmwanandroid.model.bean.UserInfo
import com.sawyer.kotlinmvvmwanandroid.util.Logger
import javax.inject.Inject

class LoginRepository @Inject constructor(){

    suspend fun login(account:String, pwd:String) : UserInfo{
        Logger.i(msg = "login_result==${RetrofitClient.apiService.login(account,pwd)}")
        return RetrofitClient.apiService.login(account,pwd).apiData()
    }

}