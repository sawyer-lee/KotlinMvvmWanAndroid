package com.sawyer.kotlinmvvmwanandroid.model.api

data class ApiResult<T>(val errorCode:Int,val errorMsg:String, private val data:T) {
    fun apiData() : T {
        if (errorCode == 0){
            return data
        }else{
            throw ApiException(errorCode,errorMsg)
        }
    }
}