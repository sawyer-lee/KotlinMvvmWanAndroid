package com.sawyer.kotlinmvvmwanandroid.ui.main.home.wechat

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.common.ScrollToTop
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.setLoadMoreStatus
import com.sawyer.kotlinmvvmwanandroid.databinding.FragmentWechatBinding
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseVmFragment
import com.sawyer.kotlinmvvmwanandroid.ui.detail.DetailActivity
import com.sawyer.kotlinmvvmwanandroid.ui.main.home.CategoryAdapter
import com.sawyer.kotlinmvvmwanandroid.ui.main.home.SimpleArticleAdapter
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.Bus
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_COLLECT_UPDATED
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_LOGIN_STATE_CHANGED
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeChatFragment : BaseVmFragment<WeChatViewModel,FragmentWechatBinding>(), ScrollToTop {

    companion object{ fun newInstance()= WeChatFragment() }

    private lateinit var mAdapterSimple: SimpleArticleAdapter
    private lateinit var mCategoryAdapter: CategoryAdapter

    override fun viewModelClass()= WeChatViewModel::class.java

    override fun initView() {
        binding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.colorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.whitePrimary)
            setOnRefreshListener { mViewModel.refreshWechatArticleList() }
        }
        mCategoryAdapter = CategoryAdapter().also {
            binding.rvCategory.adapter = it
                it.onCheckedListener ={
                    mViewModel.refreshWechatArticleList(it)
                }
            }
        mAdapterSimple = SimpleArticleAdapter().also {
            it.loadMoreModule.setOnLoadMoreListener{mViewModel.loadMoreWechatArticleList()}
            it.setOnItemClickListener { _, _, position ->
                val article = mAdapterSimple.data[position]
                ActivityHelper.start(DetailActivity::class.java, mapOf(DetailActivity.PARAM_ARTICLE to article))
            }
            it.setOnItemChildClickListener { _, view, position ->
                val article = mAdapterSimple.data[position]
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
        binding.reloadView.btnReload.setOnClickListener {
            mViewModel.getWechatCategory()
        }
        binding. reloadListView.btnReload.setOnClickListener {
            mViewModel.refreshWechatArticleList()
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
                mAdapterSimple.setList(it)
            })
            loadMoreStatus.observe(viewLifecycleOwner, Observer {
                mAdapterSimple.loadMoreModule.setLoadMoreStatus(it)
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

        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED,viewLifecycleOwner) {
            mViewModel.updateListCollectState()
        }
        Bus.observe<Pair<Long, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner) {
            mViewModel.updateItemCollectState(it)
        }
    }

    override fun lazyLoadData() { mViewModel.getWechatCategory() }

    override fun scrollToTop() { binding.recyclerView.smoothScrollToPosition(0) }
}