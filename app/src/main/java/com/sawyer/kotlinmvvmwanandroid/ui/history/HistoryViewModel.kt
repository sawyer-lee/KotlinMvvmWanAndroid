package com.sawyer.kotlinmvvmwanandroid.ui.history

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article
import com.sawyer.kotlinmvvmwanandroid.model.store.UserInfoStore
import com.sawyer.kotlinmvvmwanandroid.model.store.isLogin
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseViewModel
import com.sawyer.kotlinmvvmwanandroid.ui.common.CollectRepository
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.Bus
import com.sawyer.kotlinmvvmwanandroid.util.eventbus.USER_COLLECT_UPDATED

class HistoryViewModel @ViewModelInject constructor(
    val historyRepository: HistoryRepository,
    val collectRepository: CollectRepository
) : BaseViewModel() {

    val articleList = MutableLiveData<MutableList<Article>>()
    val emptyStatus = MutableLiveData<Boolean>()

    fun getData() {
        emptyStatus.value = false
        launch(
            block = {
                val readHistory = historyRepository.getReadHistory()
                val collectIds = UserInfoStore.getUserInfo()?.collectIds ?: emptyList<Int>()
                // 更新收藏状态
                readHistory.forEach { it.collect = collectIds.contains(it.id) }

                articleList.value = readHistory.toMutableList()
                emptyStatus.value = readHistory.isEmpty()
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

    fun deleteHistory(article: Article) {
        launch(
            block = { historyRepository.deleteHistory(article) }
        )
    }
}