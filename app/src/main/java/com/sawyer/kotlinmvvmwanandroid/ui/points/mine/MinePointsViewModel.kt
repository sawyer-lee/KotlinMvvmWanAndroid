package com.xiaojianjun.wanandroid.ui.points.mine

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.LoadMoreStatus
import com.sawyer.kotlinmvvmwanandroid.model.bean.PointRank
import com.sawyer.kotlinmvvmwanandroid.model.bean.PointRecord
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseViewModel
import com.sawyer.kotlinmvvmwanandroid.ui.points.mine.MinePointsRepository

class MinePointsViewModel @ViewModelInject constructor(val repository: MinePointsRepository): BaseViewModel() {
    companion object { const val INITIAL_PAGE = 1 }

    val totalPoints = MutableLiveData<PointRank>()
    val pointsList = MutableLiveData<MutableList<PointRecord>>()
    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    var page = INITIAL_PAGE

    fun refresh() {
        refreshStatus.value = true
        reloadStatus.value = false
        launch(
            block = {
                val points = repository.getMyPoints()
                val pagination = repository.getPointsRecord(INITIAL_PAGE)
                page = pagination.curPage
                totalPoints.value = points
                pointsList.value = pagination.datas.toMutableList()
                refreshStatus.value = false
            },
            error = {
                refreshStatus.value = false
                reloadStatus.value = page == INITIAL_PAGE
            }
        )
    }

    fun loadMoreRecord() {
        loadMoreStatus.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val pagination = repository.getPointsRecord(page + 1)
                page = pagination.curPage
                pointsList.value?.addAll(pagination.datas)
                loadMoreStatus.value = if (pagination.offset >= pagination.total) {
                    LoadMoreStatus.END
                } else {
                    LoadMoreStatus.COMPLETED
                }
            },
            error = { loadMoreStatus.value = LoadMoreStatus.ERROR }
        )
    }
}