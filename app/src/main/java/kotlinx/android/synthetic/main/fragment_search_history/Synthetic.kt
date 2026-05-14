package kotlinx.android.synthetic.main.fragment_search_history

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.zhy.view.flowlayout.TagFlowLayout

val Activity.rvSearchHistory: RecyclerView get() = findViewById(R.id.rvSearchHistory)
val Fragment.rvSearchHistory: RecyclerView get() = requireView().findViewById(R.id.rvSearchHistory)
val View.rvSearchHistory: RecyclerView get() = findViewById(R.id.rvSearchHistory)

val Activity.tflHotSearch: TagFlowLayout get() = findViewById(R.id.tflHotSearch)
val Fragment.tflHotSearch: TagFlowLayout get() = requireView().findViewById(R.id.tflHotSearch)
val View.tflHotSearch: TagFlowLayout get() = findViewById(R.id.tflHotSearch)

val Activity.tvHotSearch: TextView get() = findViewById(R.id.tvHotSearch)
val Fragment.tvHotSearch: TextView get() = requireView().findViewById(R.id.tvHotSearch)
val View.tvHotSearch: TextView get() = findViewById(R.id.tvHotSearch)

val Activity.tvSearchHistory: TextView get() = findViewById(R.id.tvSearchHistory)
val Fragment.tvSearchHistory: TextView get() = requireView().findViewById(R.id.tvSearchHistory)
val View.tvSearchHistory: TextView get() = findViewById(R.id.tvSearchHistory)
