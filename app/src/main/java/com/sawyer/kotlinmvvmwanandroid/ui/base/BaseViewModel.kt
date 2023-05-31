package com.sawyer.kotlinmvvmwanandroid.ui.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import com.sawyer.kotlinmvvmwanandroid.ext.showToast
import com.sawyer.kotlinmvvmwanandroid.model.api.ApiException
import com.sawyer.kotlinmvvmwanandroid.model.api.RetrofitClient
import com.sawyer.kotlinmvvmwanandroid.model.store.UserInfoStore
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

//kotlin中的起别名
typealias Block<T> =suspend ()->T
typealias Error=suspend (e:Exception)->Unit
typealias Cancel=suspend (e:Exception)->Unit

open class BaseViewModel : ViewModel() {
    //LiveData
    var loginStatusInvalid  = MutableLiveData<Boolean>()

    /**
     * 创建并执行协程
     * launch函数 只能用于执行一段逻辑，不能获取执行的结果，因为它的返回值永远是个Job对象
     * @param block 协程中执行
     * @param error 错误时执行
     * @return Job
     */
    protected fun launch(
        block: Block<Unit>, error: Error?=null,
        cancel: Cancel?=null, showErrorToast: Boolean = true
    ) : Job {
        return viewModelScope.launch {
            try {
                block.invoke()
            }catch (e:Exception){
                when(e){
                    is CancellationException ->{
                        cancel?.invoke(e)
                    }
                    else ->{
                        onError(e,showErrorToast)
                        //发生错误时，需要额外执行的逻辑
                        error?.invoke(e)
                    }
                }
            }
        }
    }

    /**
     * 创建并执行协程
     * async函数 必须在协程作用域中才能调用，它会创建一个新的子协程并返回一个Deferred对象，
     * 如果想要获取async函数代码块的执行结果，只需要调用Deferred对象的await()方法即可
     * @param block 协程中执行
     * @return Deferred<T>
     */
    protected fun <T> async(block: Block<T>): Deferred<T> {
        return viewModelScope.async { block.invoke() }
    }

    /**
     * 统一处理错误
     * @param e 异常
     */
    private fun onError(e: Exception,showErrorToast: Boolean){
        when(e){
            is ApiException ->{
                when(e.code){
                    -1001 ->{
                        // 登录失效，清除用户信息、清除cookie/token
                        UserInfoStore.clearUserInfo()
                        RetrofitClient.clearCookie()
                        loginStatusInvalid.value=true
                    }
                    -1 ->{//api错误
                        e.message.showToast()
                    }
                    else ->{//其他错误
                        e.message.showToast()
                    }
                }
            }
            // 网络请求失败
            is ConnectException,
            is SocketTimeoutException,
            is UnknownHostException,
            is HttpException,
            is SSLHandshakeException ->{
                if (showErrorToast) "网络连接失败".showToast()
            }
            is JsonParseException ->{
                if (showErrorToast) "数据解析错误".showToast()
            }
            else ->{
                if (showErrorToast) e.message?.showToast()
            }
        }
    }

    /**
     * 取消协程
     */
    protected fun cancelJob(job: Job?){
        if (job!=null && job.isActive && !job.isCancelled && !job.isCompleted){
            job.cancel()
        }
    }


}