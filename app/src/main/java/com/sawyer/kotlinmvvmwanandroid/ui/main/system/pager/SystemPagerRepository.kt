package com.sawyer.kotlinmvvmwanandroid.ui.main.system.pager

import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient

class SystemPagerRepository{

    suspend fun getArticleListByCid(page: Int, cid: Int) =
        RetrofitClient.apiService.getArticleListByCid(page, cid).apiData()

}