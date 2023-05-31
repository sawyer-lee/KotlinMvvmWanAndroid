package com.sawyer.kotlinmvvmwanandroid.ui.main.home.plaza

import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient
import javax.inject.Inject

class PlazaRepository @Inject constructor(){

    suspend fun getUserArticleList(page :Int)=
        RetrofitClient.apiService.getUserArticleList(page).apiData()

}