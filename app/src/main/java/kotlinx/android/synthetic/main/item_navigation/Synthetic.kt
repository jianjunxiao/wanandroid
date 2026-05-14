package kotlinx.android.synthetic.main.item_navigation

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.TextView
import com.zhy.view.flowlayout.TagFlowLayout

val Activity.tagFlawLayout: TagFlowLayout get() = findViewById(R.id.tagFlawLayout)
val Fragment.tagFlawLayout: TagFlowLayout get() = requireView().findViewById(R.id.tagFlawLayout)
val View.tagFlawLayout: TagFlowLayout get() = findViewById(R.id.tagFlawLayout)

val Activity.title: TextView get() = findViewById(R.id.title)
val Fragment.title: TextView get() = requireView().findViewById(R.id.title)
val View.title: TextView get() = findViewById(R.id.title)
