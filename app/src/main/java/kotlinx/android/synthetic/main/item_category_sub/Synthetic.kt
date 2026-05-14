package kotlinx.android.synthetic.main.item_category_sub

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.CheckedTextView

val Activity.ctvCategory: CheckedTextView get() = findViewById(R.id.ctvCategory)
val Fragment.ctvCategory: CheckedTextView get() = requireView().findViewById(R.id.ctvCategory)
val View.ctvCategory: CheckedTextView get() = findViewById(R.id.ctvCategory)
