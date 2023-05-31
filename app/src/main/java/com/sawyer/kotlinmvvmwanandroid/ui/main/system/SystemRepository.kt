package com.sawyer.kotlinmvvmwanandroid.ui.main.system

import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient
import javax.inject.Inject

class SystemRepository @Inject constructor() {

    suspend fun getArticleCategories() = RetrofitClient.apiService.getArticleCategories().apiData()

}