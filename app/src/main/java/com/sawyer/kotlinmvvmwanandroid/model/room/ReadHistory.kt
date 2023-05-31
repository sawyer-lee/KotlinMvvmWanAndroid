package com.sawyer.kotlinmvvmwanandroid.model.room

import androidx.room.Embedded
import androidx.room.Relation
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article
import com.sawyer.kotlinmvvmwanandroid.model.bean.Tag

/**
 * 将多个对象组合成一个对象。对象和对象之间是有嵌套关系的。
 * Room中你就可以使用@Embedded注解来表示嵌入
 */
data class ReadHistory(
    @Embedded
    var article: Article,
    @Relation(parentColumn = "id", entityColumn = "article_id")
    var tags: List<Tag>
)