package com.xiaojianjun.wanandroid.ui.main.discovery

import android.content.Context
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.common.core.ImageOptions
import com.xiaojianjun.wanandroid.common.core.loadImage
import com.xiaojianjun.wanandroid.model.bean.Banner
import com.youth.banner.loader.ImageLoader

/**
 * Created by xiaojianjun on 2019-12-08.
 */
class BannerImageLoader(val fragment: Fragment) : ImageLoader() {
    override fun displayImage(context: Context?, path: Any?, imageView: ImageView?) {
        val imagePath = (path as? Banner)?.imagePath
        imageView?.loadImage(
            fragment = fragment,
            url = imagePath,
            imageOptions = ImageOptions().apply {
                placeholder = R.drawable.shape_bg_image_default
            })
    }

}