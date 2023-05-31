package com.sawyer.kotlinmvvmwanandroid.model.api

import com.sawyer.kotlinmvvmwanandroid.model.bean.*
import retrofit2.http.*

interface ApiService {

    companion object{ const val BASE_URL = "https://www.wanandroid.com/" }

    //登录
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username")username:String,
        @Field("password")password:String
    ):ApiResult<UserInfo>

    //注册
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): ApiResult<UserInfo>

    //置顶文章列表
    @GET("article/top/json")
    suspend fun getTopArticleList():ApiResult<List<Article>>

    //首页文章列表
    @GET("article/list/{page}/json")
    suspend fun getArticleList(@Path("page") page: Int):ApiResult<Pagination<Article>>

    //收藏站内文章
    @POST("lg/collect/{id}/json")
    suspend fun collect(@Path("id") id:Long):ApiResult<Any?>

    //取消收藏
    @POST("lg/uncollect_originId/{id}/json")
    suspend fun unCollect(@Path("id") id: Long): ApiResult<Any?>

    //收藏列表
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectionList(@Path("page") page: Int): ApiResult<Pagination<Article>>

    //广场列表数据
    @GET("user_article/list/{page}/json")
    suspend fun getUserArticleList(@Path("page")page: Int) : ApiResult<Pagination<Article>>

    //微信公众号列表
    @GET("wxarticle/chapters/json")
    suspend fun getWechatCategories() : ApiResult<MutableList<Category>>

    //某个公众号文章列表
    @GET("wxarticle/list/{id}/{page}/json")
    suspend fun getWechatArticleList(
        @Path("page") page: Int,
        @Path("id") id: Int
    ) : ApiResult<Pagination<Article>>

    //搜索结果
    @FormUrlEncoded
    @POST("article/query/{page}/json")
    suspend fun search(
        @Field("k") keywords: String,
        @Path("page") page: Int
    ): ApiResult<Pagination<Article>>

    //搜索热词
    @GET("hotkey/json")
    suspend fun getHotWords(): ApiResult<List<HotWord>>

    //首页Banner
    @GET("banner/json")
    suspend fun getBanners(): ApiResult<List<Banner>>

    //常用网址
    @GET("friend/json")
    suspend fun getFrequentlyWebsites(): ApiResult<List<Frequently>>

    //导航
    @GET("navi/json")
    suspend fun getNavigations(): ApiResult<List<Navigation>>

    //项目分类
    @GET("project/tree/json")
    suspend fun getProjectCategories(): ApiResult<List<Category>>

    //项目列表数据
    @GET("project/list/{page}/json")
    suspend fun getProjectListByCid(
        @Path("page") page: Int,
        @Query("cid") cid : Int
    ): ApiResult<Pagination<Article>>

    //最新项目
    @GET("article/listproject/{page}/json")
    suspend fun getProjectList(@Path("page") page: Int): ApiResult<Pagination<Article>>

    //知识体系
    @GET("tree/json")
    suspend fun getArticleCategories(): ApiResult<MutableList<Category>>

    //知识体系下的文章
    @GET("article/list/{page}/json")
    suspend fun getArticleListByCid(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): ApiResult<Pagination<Article>>

    //个人积分
    @GET("lg/coin/userinfo/json")
    suspend fun getPoints(): ApiResult<PointRank>

    //积分记录
    @GET("lg/coin/list/{page}/json")
    suspend fun getPointsRecord(@Path("page") page: Int): ApiResult<Pagination<PointRecord>>

    //积分排行
    @GET("coin/rank/{page}/json")
    suspend fun getPointsRank(@Path("page") page: Int): ApiResult<Pagination<PointRank>>
}