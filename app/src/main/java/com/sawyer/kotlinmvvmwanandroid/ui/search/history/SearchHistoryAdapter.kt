package com.sawyer.kotlinmvvmwanandroid.ui.search.history

import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.BaseBindingAdapter
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.VBViewHolder
import com.sawyer.kotlinmvvmwanandroid.databinding.ItemSearchHistoryBinding

class SearchHistoryAdapter : BaseBindingAdapter<ItemSearchHistoryBinding,String>() {

    init {
        addChildClickViewIds(R.id.ivDelete)
    }

    override fun convert(holder: VBViewHolder<ItemSearchHistoryBinding>, item: String) {
        holder.vb.apply {
            tvLabel.text = item
        }
    }

}