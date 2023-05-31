package com.sawyer.kotlinmvvmwanandroid.ui.main.discovery

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.sawyer.kotlinmvvmwanandroid.model.bean.Banner
import com.sawyer.kotlinmvvmwanandroid.model.bean.Frequently
import com.sawyer.kotlinmvvmwanandroid.model.bean.HotWord
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseViewModel

class DiscoveryViewModel @ViewModelInject constructor(val repository: DiscoveryRepository): BaseViewModel() {

    val banners = MutableLiveData<List<Banner>>()
    val hotWords = MutableLiveData<List<HotWord>>()
    val frequentlyList = MutableLiveData<List<Frequently>>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    fun getData(){
        refreshStatus.value = true
        reloadStatus.value = false
        launch(
            block = {
                banners.value = repository.getBanners()
                hotWords.value = repository.getHotWords()
                frequentlyList.value = repository.getFrequentlyWebsites()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                refreshStatus.value = true
            }
        )
    }

}