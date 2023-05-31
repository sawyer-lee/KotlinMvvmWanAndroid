package com.sawyer.kotlinmvvmwanandroid.model.bean

data class Pagination<T>(
    val curPage: Int,
    val datas: List<T>,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int
)