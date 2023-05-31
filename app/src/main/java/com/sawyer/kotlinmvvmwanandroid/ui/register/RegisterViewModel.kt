package com.sawyer.kotlinmvvmwanandroid.ui.register

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.sawyer.kotlinmvvmwanandroid.model.store.UserInfoStore
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseViewModel
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.Bus
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_LOGIN_STATE_CHANGED

class RegisterViewModel @ViewModelInject constructor(val repository: RegisterRepository): BaseViewModel() {

    val submitting = MutableLiveData<Boolean>()
    val registerResult = MutableLiveData<Boolean>()

    fun register(account: String, password: String, confirmPassword: String){
        submitting.value=true
        launch(
            block = {
                val userInfo = repository.register(account, password, confirmPassword)
                UserInfoStore.setUserInfo(userInfo)
                Bus.post(USER_LOGIN_STATE_CHANGED, true)
                submitting.value=false
                registerResult.value=true
            },
            error = {
                submitting.value=false
                registerResult.value=false
            }
        )

    }

}