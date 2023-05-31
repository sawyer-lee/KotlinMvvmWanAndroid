package com.sawyer.kotlinmvvmwanandroid.model.room

import androidx.room.Room
import com.sawyer.kotlinmvvmwanandroid.App
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article
import com.sawyer.kotlinmvvmwanandroid.model.bean.Tag

/**
 * 数据库操作属于耗时的操作，所以操作的代码要加入到协程中
 */
object RoomHelper {

    private val appDatabase by lazy {
        Room.databaseBuilder(App.instance,AppDatabase::class.java,"sawyer_android").build()
    }

    private val readHistoryDao by lazy {
        appDatabase.getReadHistoryDao()
    }

    //增加阅读历史记录
    suspend fun addReadHistory(article: Article){
        readHistoryDao.queryArticle(article.id)?.let {
            readHistoryDao.deleteArticle(it)
        }
        readHistoryDao.insert(article.apply { primaryKeyId = 0 })
        //参数中传入的article的tags属性只有name和url字段
        article.tags.forEach {
            readHistoryDao.insertArticleTag(
                Tag(id = 0, articleId = article.id.toLong(),name = it.name,url = it.url)
            )
        }
    }

    //查询所有的阅读历史记录
    suspend fun queryAllReadHistory() : List<Article>{
        return readHistoryDao.queryAll().map {
            it.article.apply { tags = it.tags }
        }.reversed()
    }

    //删除阅读历史记录
    suspend fun deleteReadHistory(article: Article) = readHistoryDao.deleteArticle(article)

}