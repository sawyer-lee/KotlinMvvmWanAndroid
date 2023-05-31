package com.sawyer.kotlinmvvmwanandroid.ui.main.home.plaza

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.LoadMoreStatus
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article
import com.sawyer.kotlinmvvmwanandroid.model.store.UserInfoStore
import com.sawyer.kotlinmvvmwanandroid.model.store.isLogin
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseViewModel
import com.sawyer.kotlinmvvmwanandroid.ui.common.CollectRepository
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.Bus
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_COLLECT_UPDATED

class PlazaViewModel @ViewModelInject constructor(
    private val plazaRepository: PlazaRepository,
    private val collectRepository: CollectRepository
    ): BaseViewModel() {

    companion object{
        const val INITIAL_PAGE = 0
    }

    val articleList: MutableLiveData<MutableList<Article>> = MutableLiveData()
    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val reloadStatus = MutableLiveData<Boolean>()
    val refreshStatus = MutableLiveData<Boolean>()

    private var page= INITIAL_PAGE

    fun refreshArticleList() {
        refreshStatus.value=true
        reloadStatus.value=false
        launch(
            block = {
                val pagination = plazaRepository.getUserArticleList(INITIAL_PAGE)
                page = pagination.curPage
                articleList.value = pagination.datas.toMutableList()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = (page == INITIAL_PAGE)
            }
        )

    }

    fun loadMoreArticleList() {
        loadMoreStatus.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val pagination = plazaRepository.getUserArticleList(page)
                page = pagination.curPage
                /*val currentList = articleList.value ?: mutableListOf()
                currentList.addAll(pagination.datas)
                articleList.value = currentList*/
                articleList.value?.addAll(pagination.datas)

                loadMoreStatus.value = if(pagination.offset < pagination.total){
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

    fun collect(id: Long) {
        launch(
            block = {
                collectRepository.collect(id)
                UserInfoStore.addCollectId(id)
                updateItemCollectState(id to true)
                Bus.post(USER_COLLECT_UPDATED, id to true)
            },
            error = {
                updateItemCollectState(id to false)
            }
        )
    }

    fun unCollect(id: Long) {
        launch(
            block = {
                collectRepository.unCollect(id)
                UserInfoStore.removeCollectId(id)
                updateItemCollectState(id to false)
                Bus.post(USER_COLLECT_UPDATED, id to false)
            },
            error = {
                updateItemCollectState(id to true)
            }
        )
    }

    /**
     * 更新列表收藏状态
     */
    fun updateListCollectState() {
        val list = articleList.value
        if (list.isNullOrEmpty()) return
        if (isLogin()) {
            val collectIds = UserInfoStore.getUserInfo()?.collectIds ?: return
            list.forEach { it.collect = collectIds.contains(it.id) }
        } else {
            list.forEach { it.collect = false }
        }
        articleList.value = list
    }

    /**
     * 更新Item的收藏状态
     */
    fun updateItemCollectState(target: Pair<Long, Boolean>) {
        val list = articleList.value
        val item = list?.find { it.id == target.first } ?: return
        item.collect = target.second
        articleList.value = list
    }
}