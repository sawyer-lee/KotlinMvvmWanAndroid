package com.sawyer.kotlinmvvmwanandroid.ext

import com.sawyer.kotlinmvvmwanandroid.App
import com.sawyer.kotlinmvvmwanandroid.util.dpToPx
import com.sawyer.kotlinmvvmwanandroid.util.pxToDp

fun Float.toPx() = dpToPx(App.instance, this)

fun Float.toIntPx() = dpToPx(App.instance, this).toInt()

fun Float.toDp() = pxToDp(App.instance, this)

fun Float.toIntDp() = pxToDp(App.instance, this).toInt()