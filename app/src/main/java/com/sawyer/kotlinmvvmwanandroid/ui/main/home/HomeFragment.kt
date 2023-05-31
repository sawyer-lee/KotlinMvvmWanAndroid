package com.sawyer.kotlinmvvmwanandroid.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.sawyer.kotlinmvvmwanandroid.common.ScrollToTop
import com.sawyer.kotlinmvvmwanandroid.common.SimpleFragmentPagerAdapter
import com.sawyer.kotlinmvvmwanandroid.databinding.FragmentHomeBinding
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseBindingFragment
import com.sawyer.kotlinmvvmwanandroid.ui.main.MainActivity
import com.sawyer.kotlinmvvmwanandroid.ui.main.home.latest.LatestFragment
import com.sawyer.kotlinmvvmwanandroid.ui.main.home.plaza.PlazaFragment
import com.sawyer.kotlinmvvmwanandroid.ui.main.home.popular.PopularFragment
import com.sawyer.kotlinmvvmwanandroid.ui.main.home.project.ProjectFragment
import com.sawyer.kotlinmvvmwanandroid.ui.main.home.wechat.WeChatFragment
import com.sawyer.kotlinmvvmwanandroid.ui.search.SearchActivity
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper

class HomeFragment : BaseBindingFragment<FragmentHomeBinding>(),ScrollToTop {

    private lateinit var fragments: List<Fragment>
    private var currentOffset = 0

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragments= listOf(
            PopularFragment.newInstance(),
            LatestFragment.newInstance(),
            PlazaFragment.newInstance(),
            ProjectFragment.newInstance(),
            WeChatFragment.newInstance()
        )

        val titles = listOf("热门","最新","广场","项目","公众号")
        binding.viewPager.adapter = SimpleFragmentPagerAdapter(childFragmentManager,fragments,titles)
        binding.viewPager.offscreenPageLimit = fragments.size
        binding.tabLayout.setupWithViewPager(binding.viewPager)

        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (activity is MainActivity && this.currentOffset != verticalOffset){
                (activity as MainActivity).animateBottomNavigationView(verticalOffset > currentOffset)
                currentOffset = verticalOffset
            }
        })

        binding.llSearch.setOnClickListener { ActivityHelper.start(SearchActivity::class.java) }
    }

    override fun scrollToTop() {
        if (!this::fragments.isInitialized) return
        val currentFragment=fragments[binding.viewPager.currentItem]

        if (currentFragment is ScrollToTop && currentFragment.isVisible){
            currentFragment.scrollToTop()
        }
    }
}