package kotlinx.android.synthetic.main.item_nav_tag

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.TextView

val Activity.tvTag: TextView get() = findViewById(R.id.tvTag)
val Fragment.tvTag: TextView get() = requireView().findViewById(R.id.tvTag)
val View.tvTag: TextView get() = findViewById(R.id.tvTag)
