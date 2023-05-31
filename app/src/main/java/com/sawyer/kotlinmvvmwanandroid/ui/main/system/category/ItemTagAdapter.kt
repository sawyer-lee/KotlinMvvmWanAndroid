package com.sawyer.kotlinmvvmwanandroid.ui.main.system.category

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.model.bean.Category
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter

class ItemTagAdapter(private val categoryList: List<Category>) : TagAdapter<Category>(categoryList) {

    override fun getView(parent: FlowLayout?, position: Int, category: Category?): View {
        val view =  LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_category_sub,parent,false)
        val ctvCategory = view.findViewById<TextView>(R.id.ctvCategory)
        ctvCategory.text = categoryList[position].name
        return view
    }

}