package com.sawyer.kotlinmvvmwanandroid.util

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration

object DensityAdapterUtil {

    var sNonCompatDensity = 0f // 系统的Density
    var sNonCompatScaleDensity = 0f // 系统的ScaledDensity

    private fun setCustomDensity(activity: Activity, application: Application) {
        val appDisplayMetrics = application.resources.displayMetrics
        if (sNonCompatDensity == 0f) {
            // 系统的Density
            sNonCompatDensity = appDisplayMetrics.density
            // 系统的ScaledDensity
            sNonCompatScaleDensity = appDisplayMetrics.scaledDensity
            // 监听在系统设置中切换字体
            application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onConfigurationChanged(newConfig: Configuration) {
                    if (newConfig != null && newConfig.fontScale > 0) {
                        sNonCompatScaleDensity = application.resources.displayMetrics.scaledDensity
                    }
                }

                override fun onLowMemory() {}
            })
        }
        // 此处以360dp的设计图作为例子
        val targetDensity = (appDisplayMetrics.widthPixels / 360).toFloat()
        val targetScaledDensity = targetDensity * (sNonCompatScaleDensity / sNonCompatDensity)
        val targetDensityDpi = (160 * targetDensity).toInt()
        appDisplayMetrics.density = targetDensity
        appDisplayMetrics.scaledDensity = targetScaledDensity
        appDisplayMetrics.densityDpi = targetDensityDpi
        val activityDisplayMetrics = activity.resources.displayMetrics
        activityDisplayMetrics.density = targetDensity
        activityDisplayMetrics.scaledDensity = targetScaledDensity
        activityDisplayMetrics.densityDpi = targetDensityDpi
    }

}