package com.sawyer.kotlinmvvmwanandroid.ui.main.home.latest

import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient
import javax.inject.Inject

class LatestRepository @Inject constructor() {

    suspend fun getProjectList(page: Int) = RetrofitClient.apiService.getProjectList(page).apiData()

}