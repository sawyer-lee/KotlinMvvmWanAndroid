package com.sawyer.kotlinmvvmwanandroid.widget

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class SuppressRecyclerView : RecyclerView{

    constructor(context: Context):super(context)
    constructor(context: Context, attributeSet: AttributeSet):super(context,attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int):super(context,attributeSet,defStyleAttr)

    private var mineScrollListener:MineScrollChangeListener? = null

    interface MineScrollChangeListener{
        fun onMineScrollChanged(x:Int , y:Int , oldX:Int , oldY:Int)
    }

    fun setOnMineScrollChangeListener(mineScrollListener: MineScrollChangeListener){
        this.mineScrollListener = mineScrollListener
    }

    override fun onScrollChanged(x: Int, y: Int, oldx: Int, oldy: Int) {
        super.onScrollChanged(x, y, oldx, oldy)
        mineScrollListener?.onMineScrollChanged(x,y,oldx,oldy)
    }

}