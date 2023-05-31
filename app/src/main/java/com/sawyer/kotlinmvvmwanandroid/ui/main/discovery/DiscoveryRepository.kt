package com.sawyer.kotlinmvvmwanandroid.ui.main.discovery

import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient
import javax.inject.Inject

class DiscoveryRepository @Inject constructor(){

    suspend fun getBanners() = RetrofitClient.apiService.getBanners().apiData()

    suspend fun getHotWords() = RetrofitClient.apiService.getHotWords().apiData()

    suspend fun getFrequentlyWebsites() = RetrofitClient.apiService.getFrequentlyWebsites().apiData()

}