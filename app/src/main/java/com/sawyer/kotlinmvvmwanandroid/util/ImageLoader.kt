package com.sawyer.kotlinmvvmwanandroid.util

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import jp.wasabeef.glide.transformations.RoundedCornersTransformation

fun ImageView.loadImage(url: String?, imageOptions: ImageOptions? = null) {
    Glide.with(context)
        .load(url)
        .apply(requestOptions(imageOptions))
        /**        交叉淡入（cross fade）效果
         *  当交叉淡入被禁用时，正在过渡的图片会在原先显示的图像上面淡入。
         *  当交叉淡入被启用时，原先显示的图片会从不透明过渡到透明，而正在过渡的图片则会从透明变为不透明。
         *
         *  在 Glide 中，我们默认禁用了交叉淡入，这样通常看起来要好看一些。
         *  实际的交叉淡入，如上所述对两个图片同时改变 alpha 值，通常会在过渡的中间造成一个短暂的白色闪屏，这个时候两个图片都是部分不透明的。
         *  不幸的是，虽然禁用交叉淡入通常是一个比较好的默认行为，当待加载的图片包含透明像素时仍然可能造成问题。
         *  当占位符比实际加载的图片要大，或者图片部分为透明时，禁用交叉淡入会导致动画完成后占位符在图片后面仍然可见。
         *  如果你在加载透明图片时使用了占位符，你可以启用交叉淡入
         */
        .transition(
            DrawableTransitionOptions.with(
                DrawableCrossFadeFactory
                    .Builder(300)
                    .setCrossFadeEnabled(true)
                    .build()
            )
        )
        .into(this)
}

private fun requestOptions(imageOptions: ImageOptions?) = RequestOptions().apply {
    imageOptions?.let {
        transform(RoundedCornersTransformation(it.cornersRadius, 0))
        placeholder(it.placeholder)
        error(it.error)
        fallback(it.fallback)
        if (it.circleCrop) {
            circleCrop()
        }
    }
}

class ImageOptions {
    var placeholder = 0         // 加载占位图
    var error = 0               // 错误占位图
    var fallback = 0            // null占位图
    var cornersRadius = 0       // 圆角半径
    var circleCrop = false      // 是否裁剪为圆形
}