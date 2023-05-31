package com.sawyer.kotlinmvvmwanandroid.ui.main.home.wechat

import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient
import javax.inject.Inject

class WechatRepository @Inject constructor(){

    suspend fun getWechatCategories() = RetrofitClient.apiService.getWechatCategories().apiData()

    suspend fun getWechatArticleList(page: Int, id: Int) = RetrofitClient.apiService.getWechatArticleList(page, id).apiData()

}