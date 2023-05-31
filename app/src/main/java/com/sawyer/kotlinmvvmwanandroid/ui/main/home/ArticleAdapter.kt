package com.sawyer.kotlinmvvmwanandroid.ui.main.home

import android.view.View
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.BaseBindingAdapter
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.VBViewHolder
import com.sawyer.kotlinmvvmwanandroid.databinding.ItemArticleBinding
import com.sawyer.kotlinmvvmwanandroid.ext.htmlToSpanned
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article

class ArticleAdapter : BaseBindingAdapter<ItemArticleBinding, Article>() {

    init {
        //注意，请不要在convert方法里注册控件id
        addChildClickViewIds(R.id.iv_collect)
    }

    override fun convert(holder: VBViewHolder<ItemArticleBinding>, item: Article) {
        holder.vb.apply {
            tvAuthor.text = when {
                !item.author.isNullOrEmpty() -> {
                    item.author
                }
                !item.shareUser.isNullOrEmpty() -> {
                    item.shareUser
                }
                else -> "匿名"
            }
            tvTop.isVisible = item.top
            tvFresh.isVisible = item.fresh && !item.top
            tvTag.visibility = if (item.tags.isNotEmpty()) {
                tvTag.text = item.tags[0].name
                View.VISIBLE
            } else {
                View.GONE
            }
            tvChapter.text = when {
                !item.superChapterName.isNullOrEmpty() && !item.chapterName.isNullOrEmpty() ->
                    "${item.superChapterName.htmlToSpanned()}/${item.chapterName.htmlToSpanned()}"
                item.superChapterName.isNullOrEmpty() && !item.chapterName.isNullOrEmpty() ->
                    item.chapterName.htmlToSpanned()
                !item.superChapterName.isNullOrEmpty() && item.chapterName.isNullOrEmpty() ->
                    item.superChapterName.htmlToSpanned()
                else -> ""
            }
            tvTitle.text = item.title.htmlToSpanned()
            tvDesc.text = item.desc.htmlToSpanned()
            tvDesc.isGone = item.desc.isNullOrEmpty()
            tvTime.text = item.niceDate
            ivCollect.isSelected = item.collect

        }
    }

}