package com.sawyer.kotlinmvvmwanandroid.ui.main.home.latest

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.common.ScrollToTop
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.setLoadMoreStatus
import com.sawyer.kotlinmvvmwanandroid.databinding.FragmentLatestBinding
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseVmFragment
import com.sawyer.kotlinmvvmwanandroid.ui.detail.DetailActivity
import com.sawyer.kotlinmvvmwanandroid.ui.main.home.ArticleAdapter
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.Bus
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_COLLECT_UPDATED
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_LOGIN_STATE_CHANGED
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LatestFragment : BaseVmFragment<LatestViewModel,FragmentLatestBinding>(), ScrollToTop {

    companion object{ fun newInstance()= LatestFragment() }

    override fun viewModelClass() = LatestViewModel::class.java

    private lateinit var mAdapter: ArticleAdapter

    override fun scrollToTop() { binding.recyclerView.smoothScrollToPosition(0) }

    override fun lazyLoadData() { mViewModel.refreshProjectList()}

    override fun initView() {
        binding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.colorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.whitePrimary)
            setOnRefreshListener { mViewModel.refreshProjectList() }
        }
        binding.reloadView.btnReload.setOnClickListener {
            mViewModel.refreshProjectList()
        }
        mAdapter = ArticleAdapter().also {
            it.loadMoreModule.setOnLoadMoreListener { mViewModel.loadMoreProjectList()}
            it.setOnItemClickListener { _, _, position ->
                val article = mAdapter.data[position]
                ActivityHelper.start(
                    DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            it.setOnItemChildClickListener { _, view, position ->
                val article = mAdapter.data[position]
                if (view.id == R.id.iv_collect && checkLogin()){
                    if (article.collect) {
                        mViewModel.unCollect(article.id)
                    } else {
                        mViewModel.collect(article.id)
                    }
                }
            }
            binding.recyclerView.adapter = it
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            articleList.observe(viewLifecycleOwner, Observer {
                mAdapter.setList(it)
            })
            refreshStatus.observe(viewLifecycleOwner, Observer {
                binding.swipeRefreshLayout.isRefreshing = it
            })
            loadMoreStatus.observe(viewLifecycleOwner, Observer {
                mAdapter.loadMoreModule.setLoadMoreStatus(it)
            })
            reloadStatus.observe(viewLifecycleOwner, Observer {
                binding.reloadView.llReload.isVisible = it
            })
        }
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner) {
            mViewModel.updateListCollectState()
        }
        Bus.observe<Pair<Long, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner) {
            mViewModel.updateItemCollectState(it)
        }
    }

}