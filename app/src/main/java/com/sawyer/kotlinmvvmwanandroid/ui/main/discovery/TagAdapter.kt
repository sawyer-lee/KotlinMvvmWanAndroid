package com.sawyer.kotlinmvvmwanandroid.ui.main.discovery

import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.model.bean.Frequently
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter

class TagAdapter(private val frequentlyList: List<Frequently>) : TagAdapter<Frequently>(frequentlyList){

    override fun getView(parent: FlowLayout?, position: Int, frequently: Frequently?): View {
        val view =  LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_hot_search,parent,false)
        val tvTag = view.findViewById<TextView>(R.id.tvTag)
        tvTag.text = frequentlyList[position].name
        return view
    }

}