package com.sawyer.kotlinmvvmwanandroid.ui.main.navi

import com.sawyer.kotlinmvvmwanandroid.common.loadmore.BaseBindingAdapter
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.VBViewHolder
import com.sawyer.kotlinmvvmwanandroid.databinding.ItemNavigationBinding
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article
import com.sawyer.kotlinmvvmwanandroid.model.bean.Navigation

class NaviAdapter :BaseBindingAdapter<ItemNavigationBinding,Navigation>(){

    var onItemTagClickListener : ((article: Article) -> Unit)? = null

    override fun convert(holder: VBViewHolder<ItemNavigationBinding>, item: Navigation) {
        holder.vb.apply {
            title.text = item.name
            tagFlawLayout.adapter = ItemTagAdapter(item.articles)
            tagFlawLayout.setOnTagClickListener { _, position, _ ->
                onItemTagClickListener?.invoke(item.articles[position])
                true
            }
        }
    }

}