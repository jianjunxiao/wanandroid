package com.xiaojianjun.wanandroid.common.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.annotation.Keep
import com.xiaojianjun.wanandroid.util.getStatusBarHeight

/**
 * Created by xiaojianjun on 2020/5/6.
 * 状态栏占位控件，重写高度为状态栏高度
 */
@Keep
class StatusBarPlaceholderView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val statusBarHeight = getStatusBarHeight(context)
        val newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(statusBarHeight, MeasureSpec.EXACTLY)
        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec)
    }
}