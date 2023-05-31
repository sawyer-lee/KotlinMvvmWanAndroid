package com.sawyer.kotlinmvvmwanandroid.util

import android.content.Context

fun dpToPx(context: Context, dp: Float): Float {
//    Logger.i(msg = context.resources.displayMetrics.density.toString())
    return dp * context.resources.displayMetrics.density
}

fun pxToDp(context: Context, px: Float): Float {
    return px / context.resources.displayMetrics.density
}

