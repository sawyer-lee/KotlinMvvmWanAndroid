package com.sawyer.kotlinmvvmwanandroid.ui.search.result

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.setLoadMoreStatus
import com.sawyer.kotlinmvvmwanandroid.databinding.FragmentSearchResultBinding
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseVmFragment
import com.sawyer.kotlinmvvmwanandroid.ui.detail.DetailActivity
import com.sawyer.kotlinmvvmwanandroid.ui.main.home.ArticleAdapter
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.Bus
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_COLLECT_UPDATED
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_LOGIN_STATE_CHANGED
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchResultFragment() : BaseVmFragment<SearchResultViewModel,FragmentSearchResultBinding>() {

    companion object{ fun newInstance() = SearchResultFragment() }

    override fun viewModelClass() = SearchResultViewModel::class.java

    private lateinit var searchResultAdapter : ArticleAdapter

    override fun initView() {
        binding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.colorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.whitePrimary)
            setOnRefreshListener { mViewModel.search() }
        }

        searchResultAdapter = ArticleAdapter().also {
            it.loadMoreModule.setOnLoadMoreListener { mViewModel.loadMore() }
            it.setOnItemClickListener { _, _, position ->
                val article = it.data[position]
                ActivityHelper.start(
                    DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            it.setOnItemChildClickListener { _, view, position ->
                val article = it.data[position]
                if (view.id == R.id.iv_collect && checkLogin()){
                    view.isSelected = !view.isSelected
                    if (article.collect){
                        mViewModel.unCollect(article.id)
                    }else{
                        mViewModel.collect(article.id)
                    }
                }
            }
            binding.recyclerView.adapter = it
        }

        binding.reloadView.btnReload.setOnClickListener { mViewModel.search() }
    }

    override fun observe() {
        super.observe()
        mViewModel.apply {
            articleList.observe(viewLifecycleOwner, Observer {
                searchResultAdapter.setList(it)
            })
            refreshStatus.observe(viewLifecycleOwner, Observer {
                binding.swipeRefreshLayout.isRefreshing = it
            })
            loadMoreStatus.observe(viewLifecycleOwner, Observer {
                searchResultAdapter.loadMoreModule.setLoadMoreStatus(it)
            })
            reloadStatus.observe(viewLifecycleOwner, Observer {
                binding.reloadView.llReload.isVisible = it
            })
            emptyStatus.observe(viewLifecycleOwner, Observer {
                binding.emptyView.llEmpty.isVisible = it
            })
        }

        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED,viewLifecycleOwner) {
            mViewModel.updateListCollectState()
        }

        Bus.observe<Pair<Long,Boolean>>(USER_COLLECT_UPDATED,viewLifecycleOwner) {
            mViewModel.updateItemCollectState(it)
        }
    }

    fun doSearch(searchWords : String){
        mViewModel.search(searchWords)
    }

}