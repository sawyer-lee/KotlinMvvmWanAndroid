package com.sawyer.kotlinmvvmwanandroid.ui.main.system

import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.google.android.material.appbar.AppBarLayout
import com.sawyer.kotlinmvvmwanandroid.common.ScrollToTop
import com.sawyer.kotlinmvvmwanandroid.common.SimpleFragmentPagerAdapter
import com.sawyer.kotlinmvvmwanandroid.databinding.FragmentSystemBinding
import com.sawyer.kotlinmvvmwanandroid.model.bean.Category
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseVmFragment
import com.sawyer.kotlinmvvmwanandroid.ui.main.MainActivity
import com.sawyer.kotlinmvvmwanandroid.ui.main.system.category.SystemCategoryFragment
import com.sawyer.kotlinmvvmwanandroid.ui.main.system.pager.SystemPagerFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SystemFragment : BaseVmFragment<SystemViewModel,FragmentSystemBinding>(), ScrollToTop {

    companion object { fun newInstance() = SystemFragment() }

    override fun viewModelClass()=SystemViewModel::class.java

    private var currentOffset = 0
    private val titles = mutableListOf<String>()
    private val fragments = mutableListOf<SystemPagerFragment>()
    private var categoryFragment: SystemCategoryFragment? = null

    override fun scrollToTop() {
        if (fragments.isEmpty()) return
        fragments[binding.viewPager.currentItem].scrollToTop()
    }

    override fun initView() {
        binding.reloadView.btnReload.setOnClickListener {
            mViewModel.getArticleCategory()
        }
        binding.ivFilter.setOnClickListener {
            categoryFragment?.show(childFragmentManager)
        }
        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            if (activity is MainActivity && currentOffset != verticalOffset) {
//                LeeLog.i("偏移量=$verticalOffset")
                (activity as MainActivity).animateBottomNavigationView(verticalOffset > currentOffset)
                currentOffset = verticalOffset
            }
        })
    }

    override fun initData() { mViewModel.getArticleCategory()}

    override fun observe() {
        super.observe()
        mViewModel.run {
            categories.observe(viewLifecycleOwner, Observer {
                binding.ivFilter.visibility = View.VISIBLE
                binding.tabLayout.visibility = View.VISIBLE
                binding.viewPager.visibility = View.VISIBLE
                setup(it)
                categoryFragment = SystemCategoryFragment.newInstance(ArrayList(it))
            })
            loadingStatus.observe(viewLifecycleOwner, Observer {
                binding.progressBar.isVisible = it
            })
            reloadStatus.observe(viewLifecycleOwner, Observer {
                binding.reloadView.llReload.isVisible = it
            })
        }
    }

    private fun setup(categories: MutableList<Category>) {
        titles.clear()
        fragments.clear()
        categories.forEach {
            titles.add(it.name)
            fragments.add(SystemPagerFragment.newInstance(it.children))
        }
        binding.viewPager.adapter = SimpleFragmentPagerAdapter(childFragmentManager,fragments,titles)
        //设置当前页面任意一侧最大页面保留数，设置为集合的最大长度，避免重新创建加载页面
        binding.viewPager.offscreenPageLimit = titles.size
        binding.tabLayout.setupWithViewPager(binding.viewPager)
    }

    fun getCurrentChecked(): Pair<Int, Int> {
        if (fragments.isEmpty()) return 0 to 0
        val first = binding.viewPager.currentItem
        val second = fragments[binding.viewPager.currentItem].checkedPosition
        return first to second
    }

    fun check(position: Pair<Int, Int>) {
        if (fragments.isEmpty()) return
        binding.viewPager.currentItem = position.first
        fragments[binding.viewPager.currentItem].check(position.second)
    }

}