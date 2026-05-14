package kotlinx.android.synthetic.main.item_banner

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.ImageView

val Activity.ivBanner: ImageView get() = findViewById(R.id.ivBanner)
val Fragment.ivBanner: ImageView get() = requireView().findViewById(R.id.ivBanner)
val View.ivBanner: ImageView get() = findViewById(R.id.ivBanner)
