package com.sawyer.kotlinmvvmwanandroid.ui.points.rank

import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient
import javax.inject.Inject

class PointsRankRepository @Inject constructor(){

    suspend fun getPointsRank(page: Int) =
        RetrofitClient.apiService.getPointsRank(page).apiData()

}