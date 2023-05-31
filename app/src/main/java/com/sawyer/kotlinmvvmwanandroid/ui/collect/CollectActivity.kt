package com.sawyer.kotlinmvvmwanandroid.ui.collect

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.setLoadMoreStatus
import com.sawyer.kotlinmvvmwanandroid.databinding.ActivityCollectBinding
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseVmActivity
import com.sawyer.kotlinmvvmwanandroid.ui.detail.DetailActivity
import com.sawyer.kotlinmvvmwanandroid.ui.main.home.ArticleAdapter
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.Bus
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_COLLECT_UPDATED
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectActivity : BaseVmActivity<CollectViewModel,ActivityCollectBinding>() {

    override fun viewModelClass()=CollectViewModel::class.java

    private lateinit var mAdapter: ArticleAdapter

    override fun initView() {
        binding.swipeRefreshLayout.run {
            setColorSchemeResources(R.color.colorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.whitePrimary)
            setOnRefreshListener { mViewModel.refresh() }
        }

        mAdapter = ArticleAdapter().also {
            it.loadMoreModule.setOnLoadMoreListener { mViewModel.loadMore() }
            it.setOnItemClickListener { _, _, position ->
                val article = it.data[position]
                ActivityHelper.start(
                    DetailActivity::class.java, mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            it.setOnItemChildClickListener { _, view, position ->
                val id = mAdapter.data[position].originId
                if (view.id == R.id.iv_collect){
                    mViewModel.unCollect(id)
                }
            }
            binding.recyclerView.adapter = it
        }

        binding.reloadView.btnReload.setOnClickListener { mViewModel.refresh() }

        binding.ivBack.setOnClickListener {
            ActivityHelper.finish(CollectActivity::class.java)
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            articleList.observe(this@CollectActivity, Observer {
                mAdapter.setList(it)
            })
            refreshStatus.observe(this@CollectActivity, Observer {
                binding.swipeRefreshLayout.isRefreshing = it
            })
            loadMoreStatus.observe(this@CollectActivity, Observer {
                mAdapter.loadMoreModule.setLoadMoreStatus(it)
            })
            reloadStatus.observe(this@CollectActivity, Observer {
                binding.reloadView.llReload.isVisible = it
            })
            emptyStatus.observe(this@CollectActivity, Observer {
                binding.emptyView.llEmpty.isVisible = it
            })
        }

        Bus.observe<Pair<Long,Boolean>>(USER_COLLECT_UPDATED,this,{ (id,collect) ->
            if(collect){
                mViewModel.refresh()
            }else{
                /**
                 * indexOfFirst()
                 * 返回与给定参数[predicate]匹配的第一个元素的索引
                 * 如果列表不包含该元素，则返回-1
                 */
                val position = mAdapter.data.indexOfFirst { it.originId == id }
                if (position != -1) {
                    removeItem(position)
                }
            }
        })
    }

    override fun initData() { mViewModel.refresh() }

    private fun removeItem(position: Int) {
        mAdapter.removeAt(position)
        binding.emptyView.llEmpty.isVisible = mAdapter.data.isEmpty()
    }
}