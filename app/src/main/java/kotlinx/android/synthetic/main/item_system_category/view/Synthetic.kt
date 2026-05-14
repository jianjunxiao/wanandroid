package kotlinx.android.synthetic.main.item_system_category.view

import android.view.View
import com.xiaojianjun.wanandroid.R

import android.widget.TextView
import com.zhy.view.flowlayout.TagFlowLayout

val View.tagFlowLayout: TagFlowLayout get() = findViewById(R.id.tagFlowLayout)
val View.title: TextView get() = findViewById(R.id.title)
