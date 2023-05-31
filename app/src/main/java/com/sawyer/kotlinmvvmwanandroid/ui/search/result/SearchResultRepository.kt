package com.sawyer.kotlinmvvmwanandroid.ui.search.result

import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient
import javax.inject.Inject

class SearchResultRepository @Inject constructor() {

    suspend fun search(keywords: String, page: Int) =
        RetrofitClient.apiService.search(keywords,page).apiData()
}