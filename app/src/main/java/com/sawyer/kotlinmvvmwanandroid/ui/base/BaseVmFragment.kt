package com.sawyer.kotlinmvvmwanandroid.ui.base

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.sawyer.kotlinmvvmwanandroid.model.store.isLogin
import com.sawyer.kotlinmvvmwanandroid.ui.login.LoginActivity
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.Bus
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_LOGIN_STATE_CHANGED

abstract class BaseVmFragment<VM: BaseViewModel,VB: ViewBinding> :BaseBindingFragment<VB>() {

    protected lateinit var mViewModel:VM
    private var lazyLoaded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        observe()
        initView()
        initData()
    }

    override fun onResume() {
        super.onResume()
        // 实现懒加载
        if (!lazyLoaded) {
            lazyLoadData()
            lazyLoaded = true
        }
    }

    // Override if need
    open fun initView() { }
    open fun initData() { }
    open fun lazyLoadData() { }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(viewModelClass())
    }

    abstract fun viewModelClass(): Class<VM>

    open fun observe() {
        // 登录失效，跳转登录页
        mViewModel.loginStatusInvalid.observe(viewLifecycleOwner, Observer {
            if (it) {
                Bus.post(USER_LOGIN_STATE_CHANGED, false)
                ActivityHelper.start(LoginActivity::class.java)
            }
        })
    }

    /**
     * kotlin 高阶函数 
     * 是否登录，如果登录了就执行then，没有登录就直接跳转登录界面
     * @return true-已登录，false-未登录
     */
    fun checkLogin(then: (() -> Unit)? = null): Boolean {
        return if (isLogin()) {
            then?.invoke()
            true
        } else {
            ActivityHelper.start(LoginActivity::class.java)
            false
        }
    }

}