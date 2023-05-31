package com.sawyer.kotlinmvvmwanandroid.util

import android.app.Activity
import android.app.Application
import android.content.Intent
import com.sawyer.kotlinmvvmwanandroid.ext.putExtras
import com.sawyer.kotlinmvvmwanandroid.common.ActivityLifecycleCallbacksAdapter

object ActivityHelper {

    private val activityList = mutableListOf<Activity>()

    /** 只需关心创建和销毁即可 */
    fun init(application: Application) {
        application.registerActivityLifecycleCallbacks(ActivityLifecycleCallbacksAdapter(
            onActivityCreated = { activity, _ ->
                activityList.add(activity)
            },
            onActivityDestroyed = { activity ->
                activityList.remove(activity)
            }
        ))
    }

    /** start指定的一个Activity,并且可以传递参数 */
    fun start( clazz: Class<out Activity>, params: Map<String, Any> = emptyMap() ) {
        val currentActivity = activityList[activityList.lastIndex]
        val intent = Intent(currentActivity, clazz)
        params.forEach {
            //此为自己写的Intent的扩展函数
            intent.putExtras(it.key to it.value)
        }
        currentActivity.startActivity(intent)
    }


    /** finish指定的一个或多个Activity */
    fun finish(vararg clazz: Class<out Activity>) {
        activityList.forEach { activity ->
            if (clazz.contains(activity::class.java)) {
                activity.finish()
            }
        }
    }
}