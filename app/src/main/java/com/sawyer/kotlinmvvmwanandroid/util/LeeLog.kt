package com.sawyer.kotlinmvvmwanandroid.util

import android.util.Log

object LeeLog {

    private const val VERBOSE=1
    private const val DEBUG=2
    private const val INFO=3
    private const val WARN=4
    private const val ERROR=5

    private const val tag="lee"
    private var level= VERBOSE

    fun v(msg:String){
        if (level<= VERBOSE) Log.v(tag,msg)
    }

    fun d(msg:String){
        if (level<= DEBUG) Log.d(tag,msg)
    }

    fun i(msg:String){
        if (level<= INFO) Log.i(tag,msg)
    }
}