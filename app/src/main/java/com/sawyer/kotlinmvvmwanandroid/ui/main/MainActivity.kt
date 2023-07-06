package com.sawyer.kotlinmvvmwanandroid.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.os.Bundle
import android.view.ViewPropertyAnimator
import androidx.fragment.app.Fragment
import com.google.android.material.animation.AnimationUtils
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.common.ScrollToTop
import com.sawyer.kotlinmvvmwanandroid.databinding.ActivityMainBinding
import com.sawyer.kotlinmvvmwanandroid.ext.showToast
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseBindingActivity
import com.sawyer.kotlinmvvmwanandroid.ui.main.discovery.DiscoveryFragment
import com.sawyer.kotlinmvvmwanandroid.ui.main.home.HomeFragment
import com.sawyer.kotlinmvvmwanandroid.ui.main.mine.MineFragment
import com.sawyer.kotlinmvvmwanandroid.ui.main.navi.NaviFragment
import com.sawyer.kotlinmvvmwanandroid.ui.main.system.SystemFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseBindingActivity<ActivityMainBinding>() {

    private lateinit var mainFragments: Map<Int, Fragment>
    private var lastTimeMillis = 0L
    private var animator: ViewPropertyAnimator? = null
    private var currentBottomNavigationState = true

//    @InjectTime
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val file = File(Environment.getExternalStorageDirectory(),"catch.trace")
//        Debug.startMethodTracing(file.absolutePath)
        mainFragments = mapOf(
            R.id.home to createFragment(HomeFragment::class.java),
            R.id.system to createFragment(SystemFragment::class.java),
            R.id.discovery to createFragment(DiscoveryFragment::class.java),
            R.id.navigation to createFragment(NaviFragment::class.java),
            R.id.mine to createFragment(MineFragment::class.java)
        )
        if (savedInstanceState == null) {
            val initialItemId = R.id.home
            binding.bottomNavigation.selectedItemId = initialItemId
            showFragment(initialItemId)
        }
        binding.bottomNavigation.run {
            setOnNavigationItemSelectedListener {
                showFragment(it.itemId)
                true
            }
            setOnNavigationItemReselectedListener { menuItem ->
                val fragment = mainFragments[menuItem.itemId]
                if (fragment is ScrollToTop) {
                    fragment.scrollToTop()
                }
            }
        }
//        Debug.stopMethodTracing()
    }

    private fun createFragment(clazz: Class<out Fragment>): Fragment {
        var fragment = supportFragmentManager.fragments.find { it.javaClass == clazz }
        if (fragment == null) {
            fragment = when (clazz) {
                HomeFragment::class.java -> HomeFragment.newInstance()
                SystemFragment::class.java -> SystemFragment.newInstance()
                DiscoveryFragment::class.java -> DiscoveryFragment.newInstance()
                NaviFragment::class.java -> NaviFragment.newInstance()
                MineFragment::class.java -> MineFragment.newInstance()
                else -> throw IllegalArgumentException("argument ${clazz.simpleName} is illegal")
            }
        }
        return fragment
    }

    private fun showFragment(menuItemId: Int) {
        //当前正在显示的
        val currentFragment = supportFragmentManager.fragments.find {
            it.isVisible && it in mainFragments.values
        }
        //将要显示的
        val targetFragment = mainFragments[menuItemId]

        supportFragmentManager.beginTransaction().apply {
            currentFragment?.let {
                if (it.isVisible) hide(it)
            }
            targetFragment?.let {
                if (it.isAdded) show(it)
                else add(R.id.fl, it)
            }
        }.commit()

    }

    fun animateBottomNavigationView(show: Boolean) {
        if (currentBottomNavigationState == show) return

        if (animator != null) {
            animator?.cancel()
            binding.bottomNavigation.clearAnimation()
        }

        currentBottomNavigationState = show
        val targetY = if (show) 0F else binding.bottomNavigation.measuredHeight.toFloat()
        val duration = if (show) 225L else 175L

        animator = binding.bottomNavigation.animate()
            .translationY(targetY)
            .setDuration(duration)
            .setInterpolator(AnimationUtils.LINEAR_OUT_SLOW_IN_INTERPOLATOR)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    animator = null
                }
            })
    }

    //再按一次退出程序
    override fun onBackPressed() {
        val currentTimeMills = System.currentTimeMillis()
        if (currentTimeMills - lastTimeMillis < 2000) {
            super.onBackPressed()
        } else {
            R.string.press_again_to_exit.showToast()
            lastTimeMillis = currentTimeMills
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        animator?.cancel()  //取消当前正在运行或待处理的所有属性动画
        binding.bottomNavigation.clearAnimation()
        animator = null
    }

}