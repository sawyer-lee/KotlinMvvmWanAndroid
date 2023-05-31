package com.sawyer.kotlinmvvmwanandroid.model.api

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.sawyer.kotlinmvvmwanandroid.App
import com.sawyer.kotlinmvvmwanandroid.util.Logger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    /**Cookie*/
    private val cookiePersist = SharedPrefsCookiePersistor(App.instance)
    private val cookieJar = PersistentCookieJar(SetCookieCache(), cookiePersist)

    /**logger**/
    private val logger = HttpLoggingInterceptor.Logger {
        Logger.d(tag = this::class.simpleName, msg = it)
    }

    /**logger**/
    private val logInterceptor = HttpLoggingInterceptor(logger).apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    /**OkHttpClient*/
    private val okHttpClient = OkHttpClient.Builder()
        .callTimeout(10,TimeUnit.SECONDS)
        .cookieJar(cookieJar)
        .addNetworkInterceptor(logInterceptor)
        .build()

    /**Retrofit*/
    private val retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(ApiService.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /**ApiService*/
    val apiService = retrofit.create(ApiService::class.java)

    /**清除Cookie*/
    fun clearCookie() = cookieJar.clear()

    /**是否有Cookie*/
    fun hasCookie() = cookiePersist.loadAll().isNotEmpty()

}