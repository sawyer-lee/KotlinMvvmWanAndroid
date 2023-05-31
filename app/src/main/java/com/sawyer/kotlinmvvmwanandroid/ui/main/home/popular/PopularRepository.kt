package com.sawyer.kotlinmvvmwanandroid.ui.main.home.popular

import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article
import com.sawyer.kotlinmvvmwanandroid.model.bean.Pagination
import javax.inject.Inject

class PopularRepository @Inject constructor() {

    suspend fun getTopArticleList() = RetrofitClient.apiService.getTopArticleList().apiData()

    //Retrofit会自动进行线程的切换，即耗时任务在子线程中进行，请求的回掉结果自动切换回主线程
    /*suspend fun getTopArticleList(): List<Article>{
        return withContext(Dispatchers.IO){
            RetrofitClient.apiService.getTopArticleList().apiData()
        }
    }*/

    suspend fun getArticleList(page:Int): Pagination<Article>{
        return RetrofitClient.apiService.getArticleList(page).apiData()
    }

}