package com.sawyer.kotlinmvvmwanandroid.ui.search.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.sawyer.kotlinmvvmwanandroid.model.bean.HotWord
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseViewModel

class SearchHistoryViewModel @ViewModelInject constructor(val repository: SearchHistoryRepository): BaseViewModel(){

    val hotWords = MutableLiveData<List<HotWord>>()
    val searchHistory = MutableLiveData<MutableList<String>>()

    fun getHotSearch(){
        launch( block = { hotWords.value = repository.getHotSearch() } )
    }

    fun getSearchHistory(){
        searchHistory.value = repository.getSearchHistory()
    }

    fun addSearchHistory(searchWords: String) {
        val history = searchHistory.value ?: mutableListOf()
        if (history.contains(searchWords)){
            history.remove(searchWords)
        }
        history.add(0,searchWords)
        searchHistory.value = history
        repository.saveSearchHistory(searchWords)
    }

    fun deleteSearchHistory(searchWords: String) {
        val history = searchHistory.value ?: mutableListOf()
        if (history.contains(searchWords)){
            history.remove(searchWords)
            searchHistory.value = history
            repository.deleteSearchHistory(searchWords)
        }
    }
}