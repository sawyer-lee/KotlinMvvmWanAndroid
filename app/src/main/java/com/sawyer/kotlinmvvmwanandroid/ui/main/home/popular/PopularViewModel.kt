package com.sawyer.kotlinmvvmwanandroid.ui.main.home.popular

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

class PopularViewModel @ViewModelInject constructor(
    private val popularRepository: PopularRepository,
    private val collectRepository: CollectRepository
    ): BaseViewModel() {

    companion object { const val INITIAL_PAGE = 0 }

    var articleList = MutableLiveData<MutableList<Article>>()
    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    var refreshStatus = MutableLiveData<Boolean>()
    var reloadStatus = MutableLiveData<Boolean>()

    private var page = INITIAL_PAGE

    fun refreshArticleList() {
        refreshStatus.value = true
        reloadStatus.value = false
        launch(
            block = {
                //获取置顶文章协程执行后的结果
                val topArticleListDeferred = async { popularRepository.getTopArticleList() }
                val topArticleList = topArticleListDeferred.await().onEach {
                    it.top=true
                }
                //获取文章列表协程执行后的结果
                val paginationDeferred = async { popularRepository.getArticleList(INITIAL_PAGE) }
                val pagination = paginationDeferred.await()
                page = pagination.curPage
                //将置顶文章和文章列表添加进同一个集合
                articleList.value = mutableListOf<Article>().apply {
                    addAll(topArticleList)
                    addAll(pagination.datas)
                }
                //取消下拉刷新
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = (page == INITIAL_PAGE)
            }
        )
    }

    fun loadMoreArticleList(){
        loadMoreStatus.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val pagination= popularRepository.getArticleList(page)
                page= pagination.curPage
                val currentList= articleList.value ?: mutableListOf()
                currentList.addAll(pagination.datas)
                articleList.value= currentList

                loadMoreStatus.value= if(pagination.offset >= pagination.total){
                    LoadMoreStatus.END
                }else{
                    LoadMoreStatus.COMPLETED
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

    fun updateItemCollectState(target: Pair<Long, Boolean>) {
        val list = articleList.value
        val item = list?.find { it.id == target.first } ?: return
        item.collect = target.second
        articleList.value = list
    }
}