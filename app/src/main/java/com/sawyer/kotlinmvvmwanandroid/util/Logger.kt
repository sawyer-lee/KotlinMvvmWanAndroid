package com.sawyer.kotlinmvvmwanandroid.util

import android.util.Log
import com.sawyer.kotlinmvvmwanandroid.BuildConfig

object Logger {

    private const val defaultTag = "lee"
    private const val nullStr = "__NULL__"

    @JvmStatic
    @JvmOverloads
    fun d(tag: String? = defaultTag, msg: String?) {
        if (BuildConfig.DEBUG) {
            Log.d(tag , msg ?: nullStr)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun i(tag: String? = defaultTag, msg: String?) {
        if (BuildConfig.DEBUG) {
            Log.i(tag , msg ?: nullStr)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun e(tag: String? = defaultTag, msg: String?) {
        if (BuildConfig.DEBUG) {
            Log.e(tag , msg ?: nullStr)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun w(tag: String? = defaultTag, msg: String?) {
        if (BuildConfig.DEBUG) {
            Log.w(tag , msg ?: nullStr)
        }
    }

    @JvmStatic
    @JvmOverloads
    fun v(tag: String? = defaultTag, msg: String?) {
        if (BuildConfig.DEBUG) {
            Log.v(tag , msg ?: nullStr)
        }
    }

}