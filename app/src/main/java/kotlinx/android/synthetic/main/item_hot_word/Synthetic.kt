package kotlinx.android.synthetic.main.item_hot_word

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.TextView

val Activity.tvName: TextView get() = findViewById(R.id.tvName)
val Fragment.tvName: TextView get() = requireView().findViewById(R.id.tvName)
val View.tvName: TextView get() = findViewById(R.id.tvName)
