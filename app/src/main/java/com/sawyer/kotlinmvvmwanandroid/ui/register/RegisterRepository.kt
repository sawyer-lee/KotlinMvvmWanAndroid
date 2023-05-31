package com.sawyer.kotlinmvvmwanandroid.ui.register

import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient
import javax.inject.Inject

class RegisterRepository @Inject constructor(){
    suspend fun register(account: String, password: String, confirmPassword: String)=
        RetrofitClient.apiService.register(account,password,confirmPassword).apiData()

}