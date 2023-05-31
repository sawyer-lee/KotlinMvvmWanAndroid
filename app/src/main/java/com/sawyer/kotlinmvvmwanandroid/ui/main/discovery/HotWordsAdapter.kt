package com.sawyer.kotlinmvvmwanandroid.ui.main.discovery

import com.sawyer.kotlinmvvmwanandroid.common.loadmore.BaseBindingAdapter
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.VBViewHolder
import com.sawyer.kotlinmvvmwanandroid.databinding.ItemHotWordBinding
import com.sawyer.kotlinmvvmwanandroid.model.bean.HotWord

class HotWordsAdapter : BaseBindingAdapter<ItemHotWordBinding,HotWord>(){

    override fun convert(holder: VBViewHolder<ItemHotWordBinding>, item: HotWord) {
        holder.vb.tvName.text = item.name
    }

}