package com.sawyer.kotlinmvvmwanandroid.ui.detail

import com.sawyer.kotlinmvvmwanandroid.model.bean.Article
import com.sawyer.kotlinmvvmwanandroid.model.room.RoomHelper
import javax.inject.Inject

class DetailRepository @Inject constructor(){
    suspend fun saveReadHistory(article: Article) = RoomHelper.addReadHistory(article)
}