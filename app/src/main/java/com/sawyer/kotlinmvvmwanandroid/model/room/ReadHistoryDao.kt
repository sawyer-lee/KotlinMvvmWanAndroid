package com.sawyer.kotlinmvvmwanandroid.model.room

import androidx.room.*
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article
import com.sawyer.kotlinmvvmwanandroid.model.bean.Tag

/**
 * 注释方法@Transaction可确保在该方法中执行的所有数据库操作都将在一个事务中运行，
 * 在方法体中抛出异常时，事务将失败。
 */
@Dao
interface ReadHistoryDao {

    @Transaction
    @Insert(entity = Article::class)
    suspend fun insert(article: Article) : Long

    @Transaction
    @Insert(entity = Tag::class)
    suspend fun insertArticleTag(tag: Tag) : Long

    @Transaction
    @Query("select * from article")
    suspend fun queryAll(): List<ReadHistory>

    @Transaction
    @Query("select * from article where id = :id")
    suspend fun queryArticle(id:Long): Article?

    @Transaction
    @Delete(entity = Article::class)
    suspend fun deleteArticle(article: Article)

    @Transaction
    @Update(entity = Article::class)
    suspend fun updateArticle(article: Article)
}