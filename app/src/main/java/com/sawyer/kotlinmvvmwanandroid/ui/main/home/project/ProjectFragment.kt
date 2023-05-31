package com.sawyer.kotlinmvvmwanandroid.ui.main.home.project

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.common.ScrollToTop
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.setLoadMoreStatus
import com.sawyer.kotlinmvvmwanandroid.databinding.FragmentProjectBinding
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseVmFragment
import com.sawyer.kotlinmvvmwanandroid.ui.detail.DetailActivity
import com.sawyer.kotlinmvvmwanandroid.ui.main.home.ArticleAdapter
import com.sawyer.kotlinmvvmwanandroid.ui.main.home.CategoryAdapter
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.Bus
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_COLLECT_UPDATED
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_LOGIN_STATE_CHANGED
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProjectFragment : BaseVmFragment<ProjectViewModel,FragmentProjectBinding>(), ScrollToTop {

    companion object{ fun newInstance()= ProjectFragment() }

    override fun viewModelClass()= ProjectViewModel::class.java

    private lateinit var mAdapter: ArticleAdapter
    private lateinit var mCategoryAdapter: CategoryAdapter

    override fun scrollToTop() { binding.recyclerView.smoothScrollToPosition(0)}

    override fun lazyLoadData() { mViewModel.getProjectCategory()}

    override fun initView() {
        binding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.colorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.whitePrimary)
            setOnRefreshListener { mViewModel.refreshProjectList() }
        }
        mCategoryAdapter = CategoryAdapter().also {
            binding.rvCategory.adapter = it
            it.onCheckedListener = {
                mViewModel.refreshProjectList(it)
            }
        }
        binding.reloadListView.btnReload.setOnClickListener {
            mViewModel.refreshProjectList()
        }
        binding.reloadView.btnReload.setOnClickListener {
            mViewModel.getProjectCategory()
        }
        mAdapter = ArticleAdapter().also {
            it.loadMoreModule.setOnLoadMoreListener { mViewModel.loadMoreProjectList() }
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
                    if (article.collect){
                        mViewModel.unCollect(article.id)
                    }else{
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
            categories.observe(viewLifecycleOwner, Observer {
                binding.rvCategory.isGone = it.isEmpty()
                mCategoryAdapter.setList(it)
            })
            checkedCategory.observe(viewLifecycleOwner, Observer {
                mCategoryAdapter.check(it)
            })
            articleList.observe(viewLifecycleOwner, Observer {
                mAdapter.setList(it)
            })
            loadMoreStatus.observe(viewLifecycleOwner, Observer {
                mAdapter.loadMoreModule.setLoadMoreStatus(it)
            })
            refreshStatus.observe(viewLifecycleOwner, Observer {
                binding.swipeRefreshLayout.isRefreshing = it
            })
            reloadStatus.observe(viewLifecycleOwner, Observer {
                binding.reloadView.llReload.isVisible = it
            })
            reloadListStatus.observe(viewLifecycleOwner, Observer {
                binding.reloadListView.llReload.isVisible = it
            })
        }

        //当用户登录状态改变时
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED,viewLifecycleOwner){
            mViewModel.updateListCollectState()
        }
        //接收其他地方发送的收藏状态改变的通知
        Bus.observe<Pair<Long,Boolean>>(USER_COLLECT_UPDATED,viewLifecycleOwner){
            mViewModel.updateItemCollectState(it)
        }
    }
}