package com.sawyer.kotlinmvvmwanandroid.ui.search

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.core.view.isGone
import androidx.core.widget.addTextChangedListener
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.databinding.ActivitySearchBinding
import com.sawyer.kotlinmvvmwanandroid.ext.hideSoftInput
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseBindingActivity
import com.sawyer.kotlinmvvmwanandroid.ui.search.history.SearchHistoryFragment
import com.sawyer.kotlinmvvmwanandroid.ui.search.result.SearchResultFragment
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : BaseBindingActivity<ActivitySearchBinding>() {

    companion object {
        const val SEARCH_WORDS = "search_words"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init() {
        val historyFragment = SearchHistoryFragment.newInstance()
        val resultFragment = SearchResultFragment.newInstance()

        supportFragmentManager.beginTransaction()
            .add(R.id.flContainer,historyFragment)
            .add(R.id.flContainer,resultFragment)
            .show(historyFragment)
            .hide(resultFragment)
            .commit()

        //顶部返回按钮
        binding.ivBack.setOnClickListener {
            if (resultFragment.isVisible){
                supportFragmentManager.beginTransaction()
                    .hide(resultFragment)
                    .show(historyFragment)
                    .commit()
            }else{
                ActivityHelper.finish(SearchActivity::class.java)
            }
        }
        //搜索按钮
        binding.ivDone.setOnClickListener {
            val searchWords = binding.acetInput.text.toString()
            if (searchWords.isEmpty()) return@setOnClickListener
            it.hideSoftInput()
            historyFragment.addSearchHistory(searchWords)
            resultFragment.doSearch(searchWords)
            supportFragmentManager
                .beginTransaction()
                .hide(historyFragment)
                .show(resultFragment)
                .commit()
        }
        //输入框
        binding.acetInput.run {
            addTextChangedListener( afterTextChanged = {
                binding.ivClearSearch.isGone = it.isNullOrEmpty()
            })
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_GO){
                    binding.ivDone.performClick()
                    true
                }else{
                    false
                }
            }
        }
        //清空输入框的按钮
        binding.ivClearSearch.setOnClickListener { binding.acetInput.setText("")}
        //跳转搜索
        intent.getStringExtra(SEARCH_WORDS)?.let {
            window.decorView.post{ fillSearchInput(it) }
        }
    }

    //点击搜索历史填充输入框
    fun fillSearchInput(keywords: String) {
        binding.acetInput.setText(keywords)
        //设置光标的位置
        binding.acetInput.setSelection(keywords.length)
    }

    //响应用户点击返回的操作
    override fun onBackPressed() {
        binding.ivBack.performClick()
    }
}