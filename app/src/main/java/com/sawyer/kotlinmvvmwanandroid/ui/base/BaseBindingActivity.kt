package com.sawyer.kotlinmvvmwanandroid.ui.base

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.dylanc.viewbinding.base.inflateBindingWithGeneric
import com.sawyer.kotlinmvvmwanandroid.common.ProgressDialogFragment
/**
 *加载状态有4种：
 *      1.整页数据加载，加载动画在页面中间  2.下拉刷新  3.分页加载更多  4.数据提交服务器加载对话框
 *
 * 加载结果：
 *      1.无数据  2.无网络  3.加载失败
 */
abstract class BaseBindingActivity<VB: ViewBinding> : AppCompatActivity() {

    lateinit var binding: VB
    private lateinit var progressDialog: ProgressDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = inflateBindingWithGeneric(layoutInflater)
        setContentView(binding.root)
    }

    fun showProgressDialog(@StringRes message:Int){
        if (!this::progressDialog.isInitialized){
            progressDialog = ProgressDialogFragment.newInstance()
        }
        progressDialog.show(supportFragmentManager,message,false)
    }

    fun hideProgressDialog(){
        if (this::progressDialog.isInitialized && progressDialog.isVisible){
            progressDialog.dismissAllowingStateLoss()
        }
    }

}