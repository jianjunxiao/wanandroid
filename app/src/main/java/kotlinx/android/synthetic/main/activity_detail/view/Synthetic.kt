package kotlinx.android.synthetic.main.activity_detail.view

import android.view.View
import com.xiaojianjun.wanandroid.R

import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

val View.ivBack: ImageView get() = findViewById(R.id.ivBack)
val View.ivMore: ImageView get() = findViewById(R.id.ivMore)
val View.tvTitle: TextView get() = findViewById(R.id.tvTitle)
val View.webContainer: FrameLayout get() = findViewById(R.id.webContainer)
