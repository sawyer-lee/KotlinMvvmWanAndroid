package com.sawyer.kotlinmvvmwanandroid.ui.points.mine

import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient
import javax.inject.Inject


class MinePointsRepository @Inject constructor(){

    suspend fun getMyPoints() = RetrofitClient.apiService.getPoints().apiData()

    suspend fun getPointsRecord(page: Int) =
        RetrofitClient.apiService.getPointsRecord(page).apiData()

}