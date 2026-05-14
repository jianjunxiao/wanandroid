package kotlinx.android.synthetic.main.item_system_category

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.TextView
import com.zhy.view.flowlayout.TagFlowLayout

val Activity.tagFlowLayout: TagFlowLayout get() = findViewById(R.id.tagFlowLayout)
val Fragment.tagFlowLayout: TagFlowLayout get() = requireView().findViewById(R.id.tagFlowLayout)
val View.tagFlowLayout: TagFlowLayout get() = findViewById(R.id.tagFlowLayout)

val Activity.title: TextView get() = findViewById(R.id.title)
val Fragment.title: TextView get() = requireView().findViewById(R.id.title)
val View.title: TextView get() = findViewById(R.id.title)
