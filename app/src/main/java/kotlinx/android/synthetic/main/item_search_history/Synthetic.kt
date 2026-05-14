package kotlinx.android.synthetic.main.item_search_history

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.ImageView
import android.widget.TextView

val Activity.ivDelete: ImageView get() = findViewById(R.id.ivDelete)
val Fragment.ivDelete: ImageView get() = requireView().findViewById(R.id.ivDelete)
val View.ivDelete: ImageView get() = findViewById(R.id.ivDelete)

val Activity.ivTime: ImageView get() = findViewById(R.id.ivTime)
val Fragment.ivTime: ImageView get() = requireView().findViewById(R.id.ivTime)
val View.ivTime: ImageView get() = findViewById(R.id.ivTime)

val Activity.tvLabel: TextView get() = findViewById(R.id.tvLabel)
val Fragment.tvLabel: TextView get() = requireView().findViewById(R.id.tvLabel)
val View.tvLabel: TextView get() = findViewById(R.id.tvLabel)
