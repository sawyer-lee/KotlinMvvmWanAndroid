package com.sawyer.kotlinmvvmwanandroid.ui.points.mine

import android.annotation.SuppressLint
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.BaseBindingAdapter
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.VBViewHolder
import com.sawyer.kotlinmvvmwanandroid.databinding.ItemMinePointsBinding
import com.sawyer.kotlinmvvmwanandroid.ext.toDateTime
import com.sawyer.kotlinmvvmwanandroid.model.bean.PointRecord

@SuppressLint("SetTextI18n")
class MinePointsAdapter : BaseBindingAdapter<ItemMinePointsBinding,PointRecord>() {

    override fun convert(holder: VBViewHolder<ItemMinePointsBinding>, item: PointRecord) {
        holder.vb.run {
            tvReason.text = item.reason
            tvTime.text = item.date.toDateTime("YYYY-MM-dd HH:mm:ss")
            tvPoint.text = "+${item.coinCount}"
        }
    }

}