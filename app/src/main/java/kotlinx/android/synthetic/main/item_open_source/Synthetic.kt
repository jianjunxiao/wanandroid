package kotlinx.android.synthetic.main.item_open_source

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.TextView

val Activity.tvLink: TextView get() = findViewById(R.id.tvLink)
val Fragment.tvLink: TextView get() = requireView().findViewById(R.id.tvLink)
val View.tvLink: TextView get() = findViewById(R.id.tvLink)

val Activity.tvTitle: TextView get() = findViewById(R.id.tvTitle)
val Fragment.tvTitle: TextView get() = requireView().findViewById(R.id.tvTitle)
val View.tvTitle: TextView get() = findViewById(R.id.tvTitle)
