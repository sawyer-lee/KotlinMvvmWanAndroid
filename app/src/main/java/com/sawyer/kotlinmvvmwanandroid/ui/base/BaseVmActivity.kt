package com.sawyer.kotlinmvvmwanandroid.ui.base

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.sawyer.kotlinmvvmwanandroid.model.store.isLogin
import com.sawyer.kotlinmvvmwanandroid.ui.login.LoginActivity
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.Bus
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_LOGIN_STATE_CHANGED

abstract class BaseVmActivity<VM : BaseViewModel, VB: ViewBinding> : BaseBindingActivity<VB>() {

    protected open lateinit var mViewModel : VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        observe()
        initView()
        initData()
    }

    private fun initViewModel() {
        mViewModel = ViewModelProvider(this).get(viewModelClass())
    }

    protected abstract fun viewModelClass(): Class<VM>

    // Override if need
    open fun initView() { }

    // Override if need
    open fun initData() { }

    open fun observe(){
        // 登录失效，跳转登录页
        mViewModel.loginStatusInvalid.observe(this, Observer{
            if (it){
                Bus.post(USER_LOGIN_STATE_CHANGED,false)
                ActivityHelper.start(LoginActivity::class.java)
            }
        })
    }

    /**
     * 是否登录，如果登录了就执行then，没有登录就直接跳转登录界面
     * @return true-已登录，false-未登录
     */
    fun checkLogin(then:(()->Unit)?=null)=
        if (isLogin()){
            then?.invoke()
            true
        }else{
            ActivityHelper.start(LoginActivity::class.java)
            false
        }


}