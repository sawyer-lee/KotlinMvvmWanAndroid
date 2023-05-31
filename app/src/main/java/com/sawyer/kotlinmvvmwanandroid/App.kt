package com.sawyer.kotlinmvvmwanandroid

import android.app.Application
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.LoadMoreHelper
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper
import com.sawyer.kotlinmvvmwanandroid.util.isMainProcess
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        lateinit var instance:App
    }

    override fun onCreate() {
        super.onCreate()
//        Logger.i(msg = resources.displayMetrics.densityDpi.toString())   = 480
        instance=this
        if (isMainProcess(this)){
            init()
        }
    }

    private fun init(){
        // 在 Application 中配置全局自定义的 LoadMoreView
        LoadMoreHelper.init()
        ActivityHelper.init(this)
    }


}