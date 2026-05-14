package kotlinx.android.synthetic.main.fragment_search_history.view

import android.view.View
import com.xiaojianjun.wanandroid.R

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zhy.view.flowlayout.TagFlowLayout

val View.rvSearchHistory: RecyclerView get() = findViewById(R.id.rvSearchHistory)
val View.tflHotSearch: TagFlowLayout get() = findViewById(R.id.tflHotSearch)
val View.tvHotSearch: TextView get() = findViewById(R.id.tvHotSearch)
val View.tvSearchHistory: TextView get() = findViewById(R.id.tvSearchHistory)
