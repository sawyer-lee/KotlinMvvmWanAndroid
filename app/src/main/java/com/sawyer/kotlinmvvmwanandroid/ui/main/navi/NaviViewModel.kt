package com.sawyer.kotlinmvvmwanandroid.ui.main.navi

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.sawyer.kotlinmvvmwanandroid.model.bean.Navigation
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseViewModel

class NaviViewModel @ViewModelInject constructor(val repository: NaviRepository): BaseViewModel() {

    val navigations = MutableLiveData<List<Navigation>>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    fun getNavigations(){
        refreshStatus.value = true
        reloadStatus.value = false
        launch(
            block = {
                navigations.value = repository.getNavigations()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = navigations.value.isNullOrEmpty()
            }
        )
    }

}