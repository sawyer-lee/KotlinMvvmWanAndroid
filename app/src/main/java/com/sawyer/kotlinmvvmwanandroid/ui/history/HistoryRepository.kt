package com.sawyer.kotlinmvvmwanandroid.ui.history

import com.sawyer.kotlinmvvmwanandroid.model.bean.Article
import com.sawyer.kotlinmvvmwanandroid.model.room.RoomHelper
import javax.inject.Inject

class HistoryRepository @Inject constructor(){

    suspend fun getReadHistory() = RoomHelper.queryAllReadHistory()

    suspend fun deleteHistory(article: Article) = RoomHelper.deleteReadHistory(article)

}