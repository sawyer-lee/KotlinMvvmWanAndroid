package com.sawyer.kotlinmvvmwanandroid.model.api

class ApiException(var code: Int, override var message: String) : RuntimeException()