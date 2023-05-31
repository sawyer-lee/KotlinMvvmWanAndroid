package com.sawyer.kotlinmvvmwanandroid.ui.search.history

import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient
import com.sawyer.kotlinmvvmwanandroid.model.store.SearchHistoryStore
import javax.inject.Inject

class SearchHistoryRepository @Inject constructor(){

    suspend fun getHotSearch() = RetrofitClient.apiService.getHotWords().apiData()

    fun saveSearchHistory(searchWords: String) {
        SearchHistoryStore.saveSearchHistory(searchWords)
    }

    fun getSearchHistory() = SearchHistoryStore.getSearchHistory()


    fun deleteSearchHistory(searchWords: String) {
        SearchHistoryStore.deleteSearchHistory(searchWords)
    }

}