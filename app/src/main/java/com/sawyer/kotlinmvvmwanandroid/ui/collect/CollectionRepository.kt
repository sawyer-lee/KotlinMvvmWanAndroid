package com.sawyer.kotlinmvvmwanandroid.ui.collect

import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient
import javax.inject.Inject

class CollectionRepository @Inject constructor(){

    suspend fun getCollectionList(page: Int) =
        RetrofitClient.apiService.getCollectionList(page).apiData()

}