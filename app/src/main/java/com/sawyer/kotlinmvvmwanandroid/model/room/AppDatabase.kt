package com.sawyer.kotlinmvvmwanandroid.model.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article
import com.sawyer.kotlinmvvmwanandroid.model.bean.Tag

//需要定义3部分内容：
// 数据库的版本号、包含的实体类、提高Dao层的访问实例
@Database(version = 1 , entities = [Article::class , Tag::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun getReadHistoryDao() : ReadHistoryDao
}