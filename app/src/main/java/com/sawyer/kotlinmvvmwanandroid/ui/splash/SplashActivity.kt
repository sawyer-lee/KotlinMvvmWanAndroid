package com.sawyer.kotlinmvvmwanandroid.ui.splash

import android.os.Bundle
import com.sawyer.kotlinmvvmwanandroid.databinding.ActivitySplashBinding
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseBindingActivity
import com.sawyer.kotlinmvvmwanandroid.ui.main.MainActivity
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper

class SplashActivity : BaseBindingActivity<ActivitySplashBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.postDelayed({
            ActivityHelper.start(MainActivity::class.java)
//            ActivityHelper.start(TestActivity::class.java)
            ActivityHelper.finish(SplashActivity::class.java)
        },2000)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.lottieAnim.cancelAnimation()
    }

}