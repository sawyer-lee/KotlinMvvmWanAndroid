package com.sawyer.kotlinmvvmwanandroid.ui.main.navi

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter

class ItemTagAdapter(private val articles : List<Article>) : TagAdapter<Article>(articles) {

    override fun getView(parent: FlowLayout?, position: Int, article: Article?): View {
        val view =  LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_hot_search,parent,false)
        val tvTag = view.findViewById<TextView>(R.id.tvTag)
        tvTag.text = articles[position].title
        return view
    }

}