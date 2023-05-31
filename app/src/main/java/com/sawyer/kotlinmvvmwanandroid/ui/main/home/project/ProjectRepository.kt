package com.sawyer.kotlinmvvmwanandroid.ui.main.home.project

import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient
import javax.inject.Inject

class ProjectRepository @Inject constructor(){

    suspend fun getProjectCategories() = RetrofitClient.apiService.getProjectCategories().apiData()

    suspend fun getProjectListByCid(page: Int,cid :Int) =
        RetrofitClient.apiService.getProjectListByCid(page,cid).apiData()

}