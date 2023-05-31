package com.sawyer.kotlinmvvmwanandroid.ui.search.history

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.view.isGone
import androidx.lifecycle.Observer
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.databinding.FragmentSearchHistoryBinding
import com.sawyer.kotlinmvvmwanandroid.model.bean.HotWord
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseVmFragment
import com.sawyer.kotlinmvvmwanandroid.ui.search.SearchActivity
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchHistoryFragment : BaseVmFragment<SearchHistoryViewModel, FragmentSearchHistoryBinding>() {

    companion object {
        fun newInstance() = SearchHistoryFragment()
    }

    override fun viewModelClass() = SearchHistoryViewModel::class.java

    private lateinit var mAdapter: SearchHistoryAdapter

    override fun initView() {
        mAdapter = SearchHistoryAdapter().also {
            it.setOnItemClickListener { _, _, position ->
                (activity as? SearchActivity)?.fillSearchInput(it.data[position])
            }
            it.setOnItemChildClickListener { _, view, position ->
                if (view.id == R.id.ivDelete)
                    mViewModel.deleteSearchHistory(it.data[position])
            }
            binding.rvSearchHistory.adapter = it
        }
    }

    override fun initData() {
        mViewModel.getHotSearch()
        mViewModel.getSearchHistory()
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            hotWords.observe(viewLifecycleOwner, Observer {
                binding.tvHotSearch.visibility = View.VISIBLE
                setHotWords(it)
            })
            searchHistory.observe(viewLifecycleOwner, Observer {
                binding.tvSearchHistory.isGone = it.isEmpty()
                mAdapter.setList(it)
            })
        }
    }

    private fun setHotWords(hotWords: List<HotWord>) {
        binding.tflHotSearch.run {
            adapter = object : TagAdapter<HotWord>(hotWords) {
                override fun getView(parent: FlowLayout?, position: Int, hotWord: HotWord?): View {
                    val view = LayoutInflater.from(parent?.context)
                        .inflate(R.layout.item_hot_search, parent, false)
                    val tvTag = view.findViewById<TextView>(R.id.tvTag)
                    tvTag.text = hotWord?.name
                    return view
                }
            }
            setOnTagClickListener { _, position, _ ->
                (activity as? SearchActivity)?.fillSearchInput(hotWords[position].name)
                false
            }
        }
    }

    fun addSearchHistory(keywords: String) {
        mViewModel.addSearchHistory(keywords)
    }
}