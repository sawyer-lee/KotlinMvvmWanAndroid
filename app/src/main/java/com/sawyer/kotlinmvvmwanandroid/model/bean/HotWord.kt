package com.sawyer.kotlinmvvmwanandroid.model.bean

import androidx.annotation.Keep

@Keep
data class HotWord(
    val id: Int,
    val link: String,
    val order: Int,
    val name: String,
    val visible: Int
)