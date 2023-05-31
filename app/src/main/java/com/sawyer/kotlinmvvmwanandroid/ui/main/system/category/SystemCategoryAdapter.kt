package com.sawyer.kotlinmvvmwanandroid.ui.main.system.category

import com.sawyer.kotlinmvvmwanandroid.common.loadmore.BaseBindingAdapter
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.VBViewHolder
import com.sawyer.kotlinmvvmwanandroid.databinding.ItemSystemCategoryBinding
import com.sawyer.kotlinmvvmwanandroid.ext.htmlToSpanned
import com.sawyer.kotlinmvvmwanandroid.model.bean.Category

class SystemCategoryAdapter(
    categoryList: List<Category>,
    var checked: Pair<Int,Int>
):BaseBindingAdapter<ItemSystemCategoryBinding,Category>(categoryList.toMutableList()) {

    var onCheckedListener: ((checked: Pair<Int, Int>) -> Unit)? = null

    override fun convert(holder: VBViewHolder<ItemSystemCategoryBinding>, item: Category) {
        holder.vb.apply {
            title.text = item.name.htmlToSpanned()
            tagFlowLayout.adapter = ItemTagAdapter(item.children)
            //感觉此处作用不大，可省略，待验证
            //todo
            if (checked.first == holder.adapterPosition){
                tagFlowLayout.adapter.setSelectedList(checked.second)
            }
            tagFlowLayout.setOnTagClickListener { _, position, _ ->
                checked = holder.adapterPosition to position
                notifyDataSetChanged()
                tagFlowLayout.postDelayed({
                    onCheckedListener?.invoke(checked)
                },300)
                true
            }
        }
    }

}