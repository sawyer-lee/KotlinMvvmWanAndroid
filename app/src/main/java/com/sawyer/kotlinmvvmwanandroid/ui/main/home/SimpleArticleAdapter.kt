package com.sawyer.kotlinmvvmwanandroid.ui.main.home

import androidx.core.view.isVisible
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.BaseBindingAdapter
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.VBViewHolder
import com.sawyer.kotlinmvvmwanandroid.databinding.ItemArticleSimpleBinding
import com.sawyer.kotlinmvvmwanandroid.ext.htmlToSpanned
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article

class SimpleArticleAdapter : BaseBindingAdapter<ItemArticleSimpleBinding,Article>() {

    init {
        addChildClickViewIds(R.id.iv_collect)
    }

    override fun convert(holder: VBViewHolder<ItemArticleSimpleBinding>, item: Article) {
        holder.vb.run {
                tvAuthor.text = when {
                    !item.author.isNullOrEmpty() -> {
                        item.author
                    }
                    !item.shareUser.isNullOrEmpty() -> {
                        item.shareUser
                    }
                    else -> context.getString(R.string.anonymous)
                }
                tvFresh.isVisible = item.fresh
                tvTitle.text = item.title.htmlToSpanned()
                tvTime.text = item.niceDate
                ivCollect.isSelected = item.collect
        }
    }

}