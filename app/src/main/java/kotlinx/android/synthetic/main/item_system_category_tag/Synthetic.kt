package kotlinx.android.synthetic.main.item_system_category_tag

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.CheckedTextView

val Activity.tvTag: CheckedTextView get() = findViewById(R.id.tvTag)
val Fragment.tvTag: CheckedTextView get() = requireView().findViewById(R.id.tvTag)
val View.tvTag: CheckedTextView get() = findViewById(R.id.tvTag)
