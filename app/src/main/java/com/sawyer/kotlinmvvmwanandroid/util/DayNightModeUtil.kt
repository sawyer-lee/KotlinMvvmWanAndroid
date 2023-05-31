package com.sawyer.kotlinmvvmwanandroid.util

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES

fun isNightMode(context: Context) : Boolean{
    val flag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
    return flag == Configuration.UI_MODE_NIGHT_YES
}

fun setNightMode(isNightMode: Boolean) {
    AppCompatDelegate.setDefaultNightMode(
        if (isNightMode) MODE_NIGHT_YES
        else MODE_NIGHT_NO
    )
}