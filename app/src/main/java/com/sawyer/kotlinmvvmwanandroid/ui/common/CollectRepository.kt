package com.sawyer.kotlinmvvmwanandroid.ui.common

import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient
import javax.inject.Inject

class CollectRepository @Inject constructor() {

    suspend fun collect(id:Long)=RetrofitClient.apiService.collect(id).apiData()

    suspend fun unCollect(id:Long)=RetrofitClient.apiService.unCollect(id).apiData()

}