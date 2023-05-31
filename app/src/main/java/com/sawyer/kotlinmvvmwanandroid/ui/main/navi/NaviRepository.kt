package com.sawyer.kotlinmvvmwanandroid.ui.main.navi

import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient
import javax.inject.Inject

class NaviRepository @Inject constructor(){

    suspend fun getNavigations() =
        RetrofitClient.apiService.getNavigations().apiData()

}