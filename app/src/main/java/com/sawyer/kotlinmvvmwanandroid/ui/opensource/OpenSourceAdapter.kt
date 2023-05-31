package com.sawyer.kotlinmvvmwanandroid.ui.opensource

import com.sawyer.kotlinmvvmwanandroid.common.loadmore.BaseBindingAdapter
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.VBViewHolder
import com.sawyer.kotlinmvvmwanandroid.databinding.ItemOpenSourceBinding
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article

class OpenSourceAdapter : BaseBindingAdapter<ItemOpenSourceBinding,Article>() {

    override fun convert(holder: VBViewHolder<ItemOpenSourceBinding>, item: Article) {
        holder.vb.run {
            tvTitle.text = item.title
            tvLink.text = item.link
        }
    }

}