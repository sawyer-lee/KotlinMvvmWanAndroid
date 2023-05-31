package com.sawyer.kotlinmvvmwanandroid.ui.main.navi

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.common.ScrollToTop
import com.sawyer.kotlinmvvmwanandroid.databinding.FragmentNaviBinding
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseVmFragment
import com.sawyer.kotlinmvvmwanandroid.ui.detail.DetailActivity
import com.sawyer.kotlinmvvmwanandroid.ui.detail.DetailActivity.Companion.PARAM_ARTICLE
import com.sawyer.kotlinmvvmwanandroid.ui.main.MainActivity
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper
import com.sawyer.kotlinmvvmwanandroid.widget.SuppressRecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NaviFragment : BaseVmFragment<NaviViewModel,FragmentNaviBinding>(), ScrollToTop {

    companion object { fun newInstance() = NaviFragment() }

    override fun viewModelClass()=NaviViewModel::class.java

    private lateinit var mAdapter : NaviAdapter
    private var currentPosition = 0

    override fun scrollToTop() { binding.recyclerView.smoothScrollToPosition(0) }

    override fun initData() { mViewModel.getNavigations() }

    override fun initView() {
        binding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.colorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.whitePrimary)
            setOnRefreshListener { mViewModel.getNavigations() }
        }

        mAdapter = NaviAdapter().also {
            binding.recyclerView.adapter = it
            it.onItemTagClickListener = {
                ActivityHelper.start(
                    DetailActivity::class.java,
                    mapOf(PARAM_ARTICLE to it)
                )
            }
        }

        binding.reloadView.btnReload.setOnClickListener {
            mViewModel.getNavigations()
        }

        //此处做了处理，以便调用高版本的Api
        binding.recyclerView.setOnMineScrollChangeListener(object :
            SuppressRecyclerView.MineScrollChangeListener {
            override fun onMineScrollChanged(x: Int, y: Int, oldX: Int, oldY: Int) {
                if (activity is MainActivity && y != oldY) {
                    (activity as MainActivity).animateBottomNavigationView(y < oldY)
                }
                // y < oldY:向上滑动
                if (y < oldY) binding.tvFloatTitle.text = mAdapter.data[currentPosition].name

                val layoutManager = binding.recyclerView.layoutManager as LinearLayoutManager
                val nextView = layoutManager.findViewByPosition(currentPosition + 1)
                if (nextView != null) {
                    binding.tvFloatTitle.y = if (nextView.top < binding.tvFloatTitle.measuredHeight) {
                        (nextView.top - binding.tvFloatTitle.measuredHeight).toFloat()
                    } else {
                        0f
                    }
                }
                currentPosition = layoutManager.findFirstVisibleItemPosition()
                // y > oldY:向下滑动
                if (y > oldY) binding.tvFloatTitle.text = mAdapter.data[currentPosition].name
            }
        })

        /*recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })*/
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            navigations.observe(viewLifecycleOwner, Observer {
                binding.tvFloatTitle.isGone = it.isNullOrEmpty()
                binding.tvFloatTitle.text = it[0].name
                mAdapter.setList(it)
            })
            refreshStatus.observe(viewLifecycleOwner, Observer {
                binding.swipeRefreshLayout.isRefreshing = it
            })
            reloadStatus.observe(viewLifecycleOwner, Observer {
                binding.reloadView.llReload.isVisible = it
            })
        }

    }

}