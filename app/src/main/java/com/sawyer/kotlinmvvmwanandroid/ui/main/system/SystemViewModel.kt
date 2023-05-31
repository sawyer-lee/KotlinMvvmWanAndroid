package com.sawyer.kotlinmvvmwanandroid.ui.main.system

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.sawyer.kotlinmvvmwanandroid.model.bean.Category
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseViewModel

class SystemViewModel @ViewModelInject constructor(val repository: SystemRepository): BaseViewModel() {

    val categories = MutableLiveData<MutableList<Category>>()
    val loadingStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    fun getArticleCategory() {
        loadingStatus.value = true
        reloadStatus.value = false
        launch(
            block = {
                categories.value = repository.getArticleCategories()
                loadingStatus.value = false
            },
            error = {
                loadingStatus.value = false
                reloadStatus.value = true
            }
        )
    }

}