package com.sawyer.kotlinmvvmwanandroid.ui.history

import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.databinding.ActivityHistoryBinding
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseVmActivity
import com.sawyer.kotlinmvvmwanandroid.ui.detail.DetailActivity
import com.sawyer.kotlinmvvmwanandroid.ui.main.home.ArticleAdapter
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.Bus
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_COLLECT_UPDATED
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_LOGIN_STATE_CHANGED
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryActivity : BaseVmActivity<HistoryViewModel,ActivityHistoryBinding>() {
    companion object {
        fun newInstance(): HistoryActivity {
            return HistoryActivity()
        }
    }

    private lateinit var mAdapter: ArticleAdapter

    override fun viewModelClass() = HistoryViewModel::class.java

    override fun onResume() {
        super.onResume()
        mViewModel.getData()
    }

    override fun initView() {
        mAdapter = ArticleAdapter().also {
            it.setOnItemClickListener { _, _, position ->
                val article = it.data[position]
                ActivityHelper.start(
                    DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            it.setOnItemChildClickListener { _, view, position ->
                val article = it.data[position]
                if (view.id == R.id.iv_collect && checkLogin()) {
                    view.isSelected = !view.isSelected
                    if (article.collect) {
                        mViewModel.unCollect(article.id)
                    } else {
                        mViewModel.collect(article.id)
                    }
                }
            }
            it.setOnItemLongClickListener { _, _, position ->
                AlertDialog.Builder(this@HistoryActivity)
                    .setMessage(R.string.confirm_delete_history)
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .setPositiveButton(R.string.confirm) { _, _ ->
                        mViewModel.deleteHistory(it.data[position])
                        mAdapter.removeAt(position)
                        binding.emptyView.llEmpty.isVisible = it.data.isEmpty()
                    }.show()
                true
            }
            binding.recyclerView.adapter = it
        }
        binding.ivBack.setOnClickListener {
            ActivityHelper.finish(HistoryActivity::class.java)
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            articleList.observe(this@HistoryActivity, Observer {
                mAdapter.setList(it)
            })
            emptyStatus.observe(this@HistoryActivity, Observer {
                binding.emptyView.llEmpty.isVisible = it
            })
        }
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, this) {
            mViewModel.updateListCollectState()
        }
        Bus.observe<Pair<Long, Boolean>>(USER_COLLECT_UPDATED, this) {
            mViewModel.updateItemCollectState(it)
        }
    }
}