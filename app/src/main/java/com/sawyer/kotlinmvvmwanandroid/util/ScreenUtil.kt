package com.sawyer.kotlinmvvmwanandroid.util

import android.content.Context

fun getScreenHeight(context: Context) = context.resources.displayMetrics.heightPixels

fun getScreenWidth(context: Context) = context.resources.displayMetrics.widthPixels

fun getStatusBarHeight(context: Context): Int {
    var statusBarHeight = 0
    val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        statusBarHeight = context.resources.getDimensionPixelSize(resourceId)
    }
    return statusBarHeight
}