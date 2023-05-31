package com.sawyer.kotlinmvvmwanandroid.ui.main.home

import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.BaseBindingAdapter
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.VBViewHolder
import com.sawyer.kotlinmvvmwanandroid.databinding.ItemCategorySubBinding
import com.sawyer.kotlinmvvmwanandroid.ext.htmlToSpanned
import com.sawyer.kotlinmvvmwanandroid.ext.toIntPx
import com.sawyer.kotlinmvvmwanandroid.model.bean.Category

class CategoryAdapter : BaseBindingAdapter<ItemCategorySubBinding, Category>(){

    private var checkedPosition = 0
    var onCheckedListener : ((position:Int) -> Unit)? = null

    override fun convert(holder: VBViewHolder<ItemCategorySubBinding>, item: Category) {
        holder.vb.run {
            ctvCategory.text =item.name.htmlToSpanned()
            ctvCategory.isChecked = (checkedPosition == holder.adapterPosition)
        }
        holder.itemView.setOnClickListener {
            val position = holder.adapterPosition
            check(position)
            onCheckedListener?.invoke(position)
        }

        //作用：第一个条目增加左边距
        holder.itemView.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            marginStart = if (holder.adapterPosition == 0) 8f.toIntPx() else 0f.toIntPx()
        }
    }

    fun check(position: Int) {
        checkedPosition = position
        notifyDataSetChanged()
    }

}