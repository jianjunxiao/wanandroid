package kotlinx.android.synthetic.main.activity_detail

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView

val Activity.ivBack: ImageView get() = findViewById(R.id.ivBack)
val Fragment.ivBack: ImageView get() = requireView().findViewById(R.id.ivBack)
val View.ivBack: ImageView get() = findViewById(R.id.ivBack)

val Activity.ivMore: ImageView get() = findViewById(R.id.ivMore)
val Fragment.ivMore: ImageView get() = requireView().findViewById(R.id.ivMore)
val View.ivMore: ImageView get() = findViewById(R.id.ivMore)

val Activity.tvTitle: TextView get() = findViewById(R.id.tvTitle)
val Fragment.tvTitle: TextView get() = requireView().findViewById(R.id.tvTitle)
val View.tvTitle: TextView get() = findViewById(R.id.tvTitle)

val Activity.webContainer: FrameLayout get() = findViewById(R.id.webContainer)
val Fragment.webContainer: FrameLayout get() = requireView().findViewById(R.id.webContainer)
val View.webContainer: FrameLayout get() = findViewById(R.id.webContainer)
