package com.sawyer.kotlinmvvmwanandroid.ui.points.rank

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.setLoadMoreStatus
import com.sawyer.kotlinmvvmwanandroid.databinding.ActivityPointsRankBinding
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseVmActivity
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PointsRankActivity : BaseVmActivity<PointsRankViewModel,ActivityPointsRankBinding>() {

    private lateinit var mAdapter: PointsRankAdapter

    override fun viewModelClass() = PointsRankViewModel::class.java

    override fun initView() {
        mAdapter = PointsRankAdapter().also {
            it.loadMoreModule.setOnLoadMoreListener { mViewModel.loadMoreData() }
            binding.recyclerView.adapter = it
        }
        binding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.colorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.whitePrimary)
            setOnRefreshListener { mViewModel.refreshData() }
        }
        binding.ivBack.setOnClickListener { ActivityHelper.finish(PointsRankActivity::class.java) }
        binding.tvTitle.setOnClickListener { binding.recyclerView.smoothScrollToPosition(0) }
        binding.reloadView.btnReload.setOnClickListener { mViewModel.refreshData() }
    }

    override fun initData() { mViewModel.refreshData() }

    override fun observe() {
        super.observe()
        mViewModel.run {
            pointsRank.observe(this@PointsRankActivity, Observer {
                mAdapter.setList(it)
            })
            refreshStatus.observe(this@PointsRankActivity, Observer {
                binding.swipeRefreshLayout.isRefreshing = it
            })
            loadMoreStatus.observe(this@PointsRankActivity, Observer {
                mAdapter.loadMoreModule.setLoadMoreStatus(it)
            })
            reloadStatus.observe(this@PointsRankActivity, Observer {
                binding.reloadView.llReload.isVisible = it
            })
        }
    }

}