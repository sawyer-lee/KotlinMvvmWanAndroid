package com.sawyer.kotlinmvvmwanandroid.ui.main.discovery

import android.content.Context
import android.widget.ImageView
import com.sawyer.kotlinmvvmwanandroid.R
import com.sawyer.kotlinmvvmwanandroid.model.bean.Banner
import com.sawyer.kotlinmvvmwanandroid.util.ImageOptions
import com.sawyer.kotlinmvvmwanandroid.util.loadImage
import com.youth.banner.loader.ImageLoader

class BannerImageLoader : ImageLoader() {

    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {

        val imagePath = (path as? Banner)?.imagePath

        imageView?.loadImage(imagePath, ImageOptions().apply {
            placeholder = R.drawable.shape_bg_image_default
        })

    }

}