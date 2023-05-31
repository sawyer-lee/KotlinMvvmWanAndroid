package com.sawyer.kotlinmvvmwanandroid.ui.collect

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.LoadMoreStatus
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article
import com.sawyer.kotlinmvvmwanandroid.model.store.UserInfoStore
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseViewModel
import com.sawyer.kotlinmvvmwanandroid.ui.common.CollectRepository
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.Bus
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_COLLECT_UPDATED

class CollectViewModel @ViewModelInject constructor(
     val collectionRepository : CollectionRepository,
     val collectRepository : CollectRepository
): BaseViewModel() {
    companion object { const val INITIAL_PAGE = 0 }

    val articleList = MutableLiveData<MutableList<Article>>()
    val refreshStatus= MutableLiveData<Boolean>()
    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val reloadStatus = MutableLiveData<Boolean>()
    val emptyStatus = MutableLiveData<Boolean>()

    private var page = INITIAL_PAGE

    fun refresh(){
        refreshStatus.value = true
        reloadStatus.value = false
        emptyStatus.value = false
        launch(
            block = {
                val pagination = collectionRepository.getCollectionList(INITIAL_PAGE)
                pagination.datas.forEach { it.collect = true }
                page = pagination.curPage
                articleList.value = pagination.datas.toMutableList()
                refreshStatus.value = false
                emptyStatus.value =pagination.datas.isEmpty()
            },
            error = {
                refreshStatus.value = false
                /**
                 * 此处的判断很到位：
                 * 若第一次进来，就error的话，page 等于 INITIAL_PAGE
                 * 若不是第一次，此时error: page =1,INITIAL_PAGE=0
                 */
                reloadStatus.value = (page == INITIAL_PAGE)
            }
        )

    }

    fun loadMore(){
        loadMoreStatus.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val pagination = collectionRepository.getCollectionList(page)
                pagination.datas.forEach { it.collect = true }
                page = pagination.curPage

                val collectList  = articleList.value ?: mutableListOf()
                collectList.addAll(pagination.datas)
                articleList.value = collectList

                loadMoreStatus.value = if (pagination.offset < pagination.total){
                    LoadMoreStatus.COMPLETED
                }else{
                    LoadMoreStatus.END
                }
            },
            error = {
                loadMoreStatus.value = LoadMoreStatus.ERROR
            }
        )
    }

    fun unCollect(id : Long){
        launch(
            block = {
                collectRepository.unCollect(id)
                UserInfoStore.removeCollectId(id)
                Bus.post(USER_COLLECT_UPDATED,id to false)
            }
        )

    }
}