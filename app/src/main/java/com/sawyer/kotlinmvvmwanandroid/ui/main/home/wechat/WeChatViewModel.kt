package com.sawyer.kotlinmvvmwanandroid.ui.main.home.wechat

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.LoadMoreStatus
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article
import com.sawyer.kotlinmvvmwanandroid.model.bean.Category
import com.sawyer.kotlinmvvmwanandroid.model.store.UserInfoStore
import com.sawyer.kotlinmvvmwanandroid.model.store.isLogin
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseViewModel
import com.sawyer.kotlinmvvmwanandroid.ui.common.CollectRepository
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.Bus
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_COLLECT_UPDATED

class WeChatViewModel @ViewModelInject constructor(
    private val wechatRepository: WechatRepository,
    private val collectRepository: CollectRepository
    ):BaseViewModel() {

    companion object{
        const val INITIAL_CHECKED = 0
        const val INITIAL_PAGE = 1
    }

    val categories = MutableLiveData<MutableList<Category>> ()
    val checkedCategory = MutableLiveData<Int>()
    val articleList = MutableLiveData<MutableList<Article>>()
    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()
    val reloadListStatus = MutableLiveData<Boolean>()

    private var page = INITIAL_PAGE

    fun getWechatCategory() {
        refreshStatus.value = true
        reloadStatus.value = false
        launch(
            block = {
                val categoryList = wechatRepository.getWechatCategories()
                val checkedPosition = INITIAL_CHECKED
                val id = categoryList[checkedPosition].id
                val pagination = wechatRepository.getWechatArticleList(INITIAL_PAGE, id)
                page = pagination.curPage

                categories.value = categoryList
                checkedCategory.value = checkedPosition
                articleList.value = pagination.datas.toMutableList()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = true
            }
        )
    }

    fun refreshWechatArticleList(checkedPosition: Int = checkedCategory.value ?: INITIAL_CHECKED) {
        refreshStatus.value = true
        reloadListStatus.value = false

        if(checkedPosition != checkedCategory.value ){
            articleList.value = mutableListOf()
            checkedCategory.value = checkedPosition
        }

        launch(
            block = {
                val categoryList = categories.value ?: return@launch
                val id = categoryList[checkedPosition].id
                val pagination = wechatRepository.getWechatArticleList(INITIAL_PAGE, id)
                page = pagination.curPage
                articleList.value = pagination.datas.toMutableList()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadListStatus.value = articleList.value?.isEmpty()
            }
        )
    }

    fun loadMoreWechatArticleList() {
        loadMoreStatus.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val categoryList = categories.value ?: return@launch
                val checkedPosition = checkedCategory.value ?: return@launch
                val id = categoryList[checkedPosition].id
                val pagination = wechatRepository.getWechatArticleList(page + 1 , id)
                page = pagination.curPage

                val currentList =articleList.value ?: mutableListOf()
                currentList.addAll(pagination.datas)
                articleList.value = currentList

                loadMoreStatus.value = if (pagination.offset >= pagination.total) {
                    LoadMoreStatus.END
                } else {
                    LoadMoreStatus.COMPLETED
                }
            },
            error = {
                loadMoreStatus.value = LoadMoreStatus.ERROR
            }
        )
    }

    fun collect(id : Long){
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

    fun unCollect(id : Long){
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
     * 更新Item的收藏状态
     */
    fun updateItemCollectState(target : Pair<Long, Boolean>) {
        val list = articleList.value
        val item = list?.find { it.id == target.first } ?: return
        item.collect = target.second
        articleList.value = list
    }

    /**
     * 更新列表收藏状态
     */
    fun updateListCollectState() {
        val list = articleList.value
        if (list.isNullOrEmpty()) return
        if (isLogin()){
            val collectIds = UserInfoStore.getUserInfo()?.collectIds ?: return
            list.forEach { it.collect = collectIds.contains(it.id) }
        }else{
            list.forEach { it.collect = false }
        }
        articleList.value = list
    }
}