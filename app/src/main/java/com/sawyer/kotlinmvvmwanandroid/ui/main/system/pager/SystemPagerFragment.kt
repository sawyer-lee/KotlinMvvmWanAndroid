package com.sawyer.kotlinmvvmwanandroid.ui.main.system.pager

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.common.ScrollToTop
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.setLoadMoreStatus
import com.sawyer.kotlinmvvmwanandroid.databinding.FragmentSystemPagerBinding
import com.sawyer.kotlinmvvmwanandroid.ext.toIntPx
import com.sawyer.kotlinmvvmwanandroid.model.bean.Category
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseVmFragment
import com.sawyer.kotlinmvvmwanandroid.ui.detail.DetailActivity
import com.sawyer.kotlinmvvmwanandroid.ui.main.home.CategoryAdapter
import com.sawyer.kotlinmvvmwanandroid.ui.main.home.SimpleArticleAdapter
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.Bus
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_COLLECT_UPDATED
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_LOGIN_STATE_CHANGED

class SystemPagerFragment : BaseVmFragment<SystemPagerViewModel,FragmentSystemPagerBinding>(),ScrollToTop {
    companion object{
        private const val CATEGORY_LIST = "CATEGORY_LIST"
        fun newInstance(categoryList: ArrayList<Category>) : SystemPagerFragment{
            return SystemPagerFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(CATEGORY_LIST,categoryList)
                }
            }
        }
    }

    private lateinit var categoryList: List<Category>
    private lateinit var mAdapter: SimpleArticleAdapter
    private lateinit var categoryAdapter: CategoryAdapter
    var checkedPosition = 0

    override fun viewModelClass() = SystemPagerViewModel::class.java

    override fun scrollToTop() { binding.recyclerView.smoothScrollToPosition(0) }

    override fun lazyLoadData() { mViewModel.refreshArticleList(categoryList[checkedPosition].id) }

    override fun initView() {
        binding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.colorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.whitePrimary)
            setOnRefreshListener {
                mViewModel.refreshArticleList(categoryList[checkedPosition].id)
            }
        }
        binding.reloadView.btnReload.setOnClickListener {
            mViewModel.refreshArticleList(categoryList[checkedPosition].id)
        }
        categoryList = arguments?.getParcelableArrayList(CATEGORY_LIST)!!
        checkedPosition = 0
        categoryAdapter = CategoryAdapter().also {
            binding.rvCategory.adapter = it
            it.setNewInstance(categoryList.toMutableList())
            it.onCheckedListener = { position ->
                checkedPosition = position
                mViewModel.refreshArticleList(categoryList[checkedPosition].id)
            }
        }
        mAdapter = SimpleArticleAdapter().also {
            it.loadMoreModule.setOnLoadMoreListener {
                mViewModel.loadMoreArticleList(categoryList[checkedPosition].id)
            }
            it.setOnItemClickListener { _, _, position ->
                val article = mAdapter.data[position]
                ActivityHelper.start(
                    DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            it.setOnItemChildClickListener { _, view, position ->
                val article = mAdapter.data[position]
                if (view.id == R.id.iv_collect && checkLogin()) {
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
            loadMoreStatus.observe(viewLifecycleOwner, Observer {
                mAdapter.loadMoreModule.setLoadMoreStatus(it)
            })
            refreshStatus.observe(viewLifecycleOwner, Observer {
                binding.swipeRefreshLayout.isRefreshing = it
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

    fun check(position: Int) {
        if (position != checkedPosition) {
            checkedPosition = position
            categoryAdapter.check(position)
            (binding.rvCategory.layoutManager as LinearLayoutManager)
                .scrollToPositionWithOffset(position , 8f.toIntPx())
            mViewModel.refreshArticleList(categoryList[checkedPosition].id)
        }
    }
}