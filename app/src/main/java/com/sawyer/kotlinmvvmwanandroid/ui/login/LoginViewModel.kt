package com.sawyer.kotlinmvvmwanandroid.ui.login

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.sawyer.kotlinmvvmwanandroid.model.store.UserInfoStore
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseViewModel
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.Bus
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_LOGIN_STATE_CHANGED

class LoginViewModel @ViewModelInject constructor(
    val repository: LoginRepository): BaseViewModel() {

    val submitting = MutableLiveData<Boolean>()
    val loginResult = MutableLiveData<Boolean>()

    fun login(account:String,pwd:String){
        submitting.value=true
        launch(
            block = {
                val userInfo = repository.login(account, pwd)
                Log.i("lee", "login_userInfo====$userInfo")
                UserInfoStore.setUserInfo(userInfo)
                Bus.post(USER_LOGIN_STATE_CHANGED,true)
                submitting.value=false
                loginResult.value=true
            },
            error={
                submitting.value=false
                loginResult.value=false
            }
        )
    }

}