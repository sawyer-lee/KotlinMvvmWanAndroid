package com.sawyer.kotlinmvvmwanandroid.ui.points.mine

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.setLoadMoreStatus
import com.sawyer.kotlinmvvmwanandroid.databinding.ActivityMinePointsBinding
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseVmActivity
import com.sawyer.kotlinmvvmwanandroid.ui.points.rank.PointsRankActivity
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper
import com.xiaojianjun.wanandroid.ui.points.mine.MinePointsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MinePointsActivity : BaseVmActivity<MinePointsViewModel,ActivityMinePointsBinding>() {

    private lateinit var mAdapter: MinePointsAdapter
    private lateinit var mHeaderView: View

    override fun viewModelClass() = MinePointsViewModel::class.java

    override fun initView() {
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.header_mine_points, null)
        mAdapter = MinePointsAdapter().also {
            it.loadMoreModule.setOnLoadMoreListener { mViewModel.loadMoreRecord() }
            binding.recyclerView.adapter = it
        }
        binding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.colorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.whitePrimary)
            setOnRefreshListener { mViewModel.refresh() }
        }
        binding.ivBack.setOnClickListener { ActivityHelper.finish(MinePointsActivity::class.java) }
        binding.ivRank.setOnClickListener { ActivityHelper.start(PointsRankActivity::class.java) }
        binding.reloadView.btnReload.setOnClickListener { mViewModel.refresh() }
    }

    override fun initData() { mViewModel.refresh() }

    override fun observe() {
        super.observe()
        mViewModel.run {
            totalPoints.observe(this@MinePointsActivity, Observer {
                if (mAdapter.headerLayoutCount == 0) {
                    mAdapter.setHeaderView(mHeaderView)
                }
                mHeaderView.findViewById<TextView>(R.id.tvTotalPoints).text = it.coinCount.toString()
                mHeaderView.findViewById<TextView>(R.id.tvLevelRank).text = getString(R.string.level_rank, it.level, it.rank)

            })
            pointsList.observe(this@MinePointsActivity, Observer {
                mAdapter.setList(it)
            })
            refreshStatus.observe(this@MinePointsActivity, Observer {
                binding.swipeRefreshLayout.isRefreshing = it
            })
            loadMoreStatus.observe(this@MinePointsActivity, Observer {
                mAdapter.loadMoreModule.setLoadMoreStatus(it)
            })
            reloadStatus.observe(this@MinePointsActivity, Observer {
                binding.reloadView.llReload.isVisible = it
            })
        }
    }

}