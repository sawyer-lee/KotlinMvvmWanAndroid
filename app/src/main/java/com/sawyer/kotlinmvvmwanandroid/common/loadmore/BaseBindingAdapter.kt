package com.sawyer.kotlinmvvmwanandroid.common.loadmore

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import java.lang.reflect.ParameterizedType

/**
 *自定义Adapter,继承自BaseQuickAdapter
 * 扩展了ViewBinding、自定义LoadMore的功能
 */
class VBViewHolder<VB: ViewBinding>(val vb: VB) : BaseViewHolder(vb.root)

@Suppress("UNCHECKED_CAST")
abstract class BaseBindingAdapter<VB: ViewBinding , T>(
    data: MutableList<T>? = null
) : BaseQuickAdapter<T,VBViewHolder<VB>>(0,data),LoadMoreModule{

    //重写返回自定义 ViewHolder
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): VBViewHolder<VB> {
        //这里为了使用简洁性，使用反射来实例ViewBinding
        val vbClass: Class<VB> =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<VB>

        val method = vbClass.getDeclaredMethod(
            "inflate", LayoutInflater::class.java,
            ViewGroup::class.java, Boolean::class.java
        )

        val mBinding = method.invoke(null,LayoutInflater.from(parent.context),parent,false) as VB
        return VBViewHolder(mBinding)
    }

}