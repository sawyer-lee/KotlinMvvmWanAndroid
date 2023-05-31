package com.sawyer.kotlinmvvmwanandroid.ui.points.rank

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import com.sawyer.kotlinmvvmwanandroid.common.loadmore.LoadMoreStatus
import com.sawyer.kotlinmvvmwanandroid.model.bean.PointRank
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseViewModel

class PointsRankViewModel @ViewModelInject constructor(val repository: PointsRankRepository): BaseViewModel() {
    companion object {
        const val INITIAL_PAGE = 1
    }

    val pointsRank = MutableLiveData<MutableList<PointRank>>()
    val loadMoreStatus = MutableLiveData<LoadMoreStatus>()
    val refreshStatus = MutableLiveData<Boolean>()
    val reloadStatus = MutableLiveData<Boolean>()

    private var page = INITIAL_PAGE

    fun refreshData() {
        refreshStatus.value = true
        reloadStatus.value = false
        launch(
            block = {
                val pagination = repository.getPointsRank(INITIAL_PAGE)
                page = pagination.curPage
                pointsRank.value = pagination.datas.toMutableList()
                refreshStatus.value = false
            },
            error = {
                reloadStatus.value = page == INITIAL_PAGE
            }
        )
    }

    fun loadMoreData() {
        loadMoreStatus.value = LoadMoreStatus.LOADING
        launch(
            block = {
                val pagination = repository.getPointsRank(page + 1)
                page = pagination.curPage
                pointsRank.value?.addAll(pagination.datas)
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