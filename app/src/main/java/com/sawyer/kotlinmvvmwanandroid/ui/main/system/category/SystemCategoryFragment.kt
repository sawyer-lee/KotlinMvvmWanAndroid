package com.sawyer.kotlinmvvmwanandroid.ui.main.system.category

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sawyer.kotlinmvvmwanandroid.App
import com.sawyer.kotlinmvvmwanandroid.databinding.FragmentSystemCategoryBinding
import com.sawyer.kotlinmvvmwanandroid.model.bean.Category
import com.sawyer.kotlinmvvmwanandroid.ui.main.system.SystemFragment
import com.sawyer.kotlinmvvmwanandroid.util.getScreenHeight

class SystemCategoryFragment : BottomSheetDialogFragment() {

    companion object{
        const val CATEGORY_LIST = "categoryList"
        fun newInstance(categoryList: ArrayList<Category>): SystemCategoryFragment{
            return SystemCategoryFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(CATEGORY_LIST , categoryList)
                }
            }
        }
    }

    private var _binding : FragmentSystemCategoryBinding? = null
    private val binding get() = _binding!!
    private var height: Int? = null
    private var behavior: BottomSheetBehavior<View>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSystemCategoryBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryList : ArrayList<Category> = arguments?.getParcelableArrayList(CATEGORY_LIST)!!
        val checked = (parentFragment as SystemFragment).getCurrentChecked()
        SystemCategoryAdapter(categoryList,checked).also {
            it.onCheckedListener= {
                behavior?.state = BottomSheetBehavior.STATE_HIDDEN
                view.postDelayed({
                    (parentFragment as SystemFragment).check(it)
                },300)
            }
            binding.recyclerView.adapter = it
        }
        //固定底部弹框的选中位置
        view.post{
            (binding.recyclerView.layoutManager as LinearLayoutManager)
                .scrollToPositionWithOffset(checked.first,0)
        }
    }

    override fun onStart() {
        super.onStart()
        val bottomSheet:View = (dialog as BottomSheetDialog).delegate
            .findViewById(com.google.android.material.R.id.design_bottom_sheet)
            ?: return
        behavior = BottomSheetBehavior.from(bottomSheet)
        height?.let {
            behavior?.peekHeight = it
        }
        //设置底部弹框的布局位置以及大小
        dialog?.window?.let {
            it.setGravity(Gravity.BOTTOM)
            it.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, height?:ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    fun show(manager: FragmentManager, height: Int? = null){
        //设置底部弹框高度，默认为屏幕高度的3/4
        this.height = height ?: (getScreenHeight(App.instance) * 0.75f).toInt()
        if (!this.isAdded){
            super.show(manager,"SystemCategoryFragment")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}