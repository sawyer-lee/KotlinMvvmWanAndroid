package com.sawyer.kotlinmvvmwanandroid.ui.opensource

import android.os.Bundle
import com.sawyer.kotlinmvvmwanandroid.databinding.ActivityOpenSourceBinding
import com.sawyer.kotlinmvvmwanandroid.model.bean.Article
import com.sawyer.kotlinmvvmwanandroid.ui.base.BaseBindingActivity
import com.sawyer.kotlinmvvmwanandroid.ui.detail.DetailActivity
import com.sawyer.kotlinmvvmwanandroid.ui.detail.DetailActivity.Companion.PARAM_ARTICLE
import com.sawyer.kotlinmvvmwanandroid.util.ActivityHelper

class OpenSourceActivity : BaseBindingActivity<ActivityOpenSourceBinding>() {

    private val openSourceData = listOf(
        Article(
            title = "OkHttp",
            link = "https://github.com/square/okhttp"
        ),
        Article(
            title = "Retrofit",
            link = "https://github.com/square/retrofit"
        ),
        Article(
            title = "BaseRecyclerViewAdapterHelper",
            link = "https://github.com/CymChad/BaseRecyclerViewAdapterHelper"
        ),
        Article(
            title = "FlowLayout",
            link = "https://github.com/hongyangAndroid/FlowLayout"
        ),
        Article(
            title = "Banner",
            link = "https://github.com/youth5201314/banner"
        ),
        Article(
            title = "Glide",
            link = "https://github.com/bumptech/glide"
        ),
        Article(
            title = "Glide-Tansformations",
            link = "https://github.com/wasabeef/glide-transformations"
        ),
        Article(
            title = "AgentWeb",
            link = "https://github.com/Justson/AgentWeb"
        ),
        Article(
            title = "LiveEventBus",
            link = "https://github.com/JeremyLiao/LiveEventBus"
        ),
        Article(
            title = "PersistentCookieJar",
            link = "https://github.com/franmontiel/PersistentCookieJar"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        OpenSourceAdapter().also {
            binding.recyclerView.adapter = it
            it.setList(openSourceData)
            it.setOnItemClickListener { _, _, position ->
                val article = it.data[position]
                ActivityHelper.start(DetailActivity::class.java, mapOf(PARAM_ARTICLE to article))
            }
        }

        binding.ivBack.setOnClickListener { ActivityHelper.finish(OpenSourceActivity::class.java) }
    }
}