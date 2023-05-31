package com.sawyer.kotlinmvvmwanandroid.ui.detail

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article
import com.sawyer.kotlinmvvmwanandroid.model.store.UserInfoStore
import com.sawyer.kotlinmvvmwanandroid.model.store.isLogin
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseViewModel
import com.sawyer.kotlinmvvmwanandroid.ui.common.CollectRepository

class DetailViewModel @ViewModelInject constructor(
    val collectRepository : CollectRepository,
    val detailRepository : DetailRepository
): BaseViewModel() {

    val collect = MutableLiveData<Boolean>()

    fun collect(id : Long){
        launch(
            block = {
                collectRepository.collect(id)
                UserInfoStore.addCollectId(id)
                collect.value = true
            },
            error = {
                collect.value = false
            }
        )
    }

    fun unCollect(id : Long){
        launch(
            block = {
                collectRepository.unCollect(id)
                UserInfoStore.removeCollectId(id)
                collect.value = false
            },
            error = {
                collect.value = true
            }
        )
    }

    fun updateCollectStatus(id: Long) {
        collect.value = if (isLogin()){
            UserInfoStore.getUserInfo()?.collectIds?.contains(id)
        }else{
            false
        }
    }

    fun saveReadHistory(article: Article){
        launch( block = { detailRepository.saveReadHistory(article) } )
    }

}