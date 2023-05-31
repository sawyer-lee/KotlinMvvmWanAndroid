package com.sawyer.kotlinmvvmwanandroid.ui.main.home.plaza

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.common.ScrollToTop
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.setLoadMoreStatus
import com.sawyer.kotlinmvvmwanandroid.databinding.FragmentPlazaBinding
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseVmFragment
import com.sawyer.kotlinmvvmwanandroid.ui.detail.DetailActivity
import com.sawyer.kotlinmvvmwanandroid.ui.main.home.SimpleArticleAdapter
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.Bus
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_COLLECT_UPDATED
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_LOGIN_STATE_CHANGED
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlazaFragment : BaseVmFragment<PlazaViewModel,FragmentPlazaBinding>(), ScrollToTop {

    companion object{
        fun newInstance()= PlazaFragment()
    }

    private lateinit var mSimpleAdapter : SimpleArticleAdapter

    override fun viewModelClass()= PlazaViewModel::class.java

    override fun lazyLoadData() { mViewModel.refreshArticleList()}

    override fun scrollToTop() { binding.recyclerView.smoothScrollToPosition(0) }

    override fun initView() {
        binding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.colorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.whitePrimary)
            setOnRefreshListener { mViewModel.refreshArticleList() }
        }

        mSimpleAdapter = SimpleArticleAdapter().also {
            it.loadMoreModule.setOnLoadMoreListener { mViewModel.loadMoreArticleList() }
            it.setOnItemClickListener { _, _, position ->
                val article = mSimpleAdapter.data[position]
                ActivityHelper.start(
                    DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            it.setOnItemChildClickListener { _, view, position ->
                val article = mSimpleAdapter.data[position]
                if (view.id == R.id.iv_collect && checkLogin()) {
                    view.isSelected = !view.isSelected
                    if (article.collect) {
                        mViewModel.unCollect(article.id)
                    } else {
                        mViewModel.collect(article.id)
                    }
                }
            }
            binding.recyclerView.adapter = it
        }

        binding.reloadView.btnReload.setOnClickListener { mViewModel.refreshArticleList() }
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            articleList.observe(viewLifecycleOwner, Observer {
                mSimpleAdapter.setList(it)
            })
            refreshStatus.observe(viewLifecycleOwner, Observer {
                binding.swipeRefreshLayout.isRefreshing = it
            })
            reloadStatus.observe(viewLifecycleOwner, Observer {
                binding.reloadView.llReload.isVisible = it
            })
            loadMoreStatus.observe(viewLifecycleOwner, Observer {
               mSimpleAdapter.loadMoreModule.setLoadMoreStatus(it)
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