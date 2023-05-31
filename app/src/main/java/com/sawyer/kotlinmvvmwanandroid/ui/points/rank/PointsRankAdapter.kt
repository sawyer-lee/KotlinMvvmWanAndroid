package com.sawyer.kotlinmvvmwanandroid.ui.points.rank

import android.annotation.SuppressLint
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.BaseBindingAdapter
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.VBViewHolder
import com.sawyer.kotlinmvvmwanandroid.databinding.ItemPointsRankBinding
import com.sawyer.kotlinmvvmwanandroid.model.bean.PointRank

@SuppressLint("SetTextI18n")
class PointsRankAdapter : BaseBindingAdapter<ItemPointsRankBinding,PointRank>() {

    override fun convert(holder: VBViewHolder<ItemPointsRankBinding>, item: PointRank) {
        holder.vb.run {
            tvNo.text = "${holder.adapterPosition + 1}"
            tvName.text = item.username
            tvPoints.text = item.coinCount.toString()
        }
    }

}