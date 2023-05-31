package com.sawyer.kotlinmvvmwanandroid.util

import android.app.ActivityManager
import android.content.Context
import android.os.Process

fun isMainProcess(context: Context) = context.packageName == currentProcessName(context)

/**
 * 当前进程的名称
 */
private fun currentProcessName(context: Context): String {
    val manager=context.getSystemService(Context.ACTIVITY_SERVICE)as ActivityManager
    for (process in manager.runningAppProcesses){
        if (process.pid==Process.myPid()){
//            LogUtil.i("当前线程名字为："+process.processName)
            return process.processName
        }
    }
    return ""
}