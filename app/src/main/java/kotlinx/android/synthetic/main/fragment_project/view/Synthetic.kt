package kotlinx.android.synthetic.main.fragment_project.view

import android.view.View
import com.xiaojianjun.wanandroid.R

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

val View.recyclerView: RecyclerView get() = findViewById(R.id.recyclerView)
val View.reloadListView: View get() = findViewById(R.id.reloadListView)
val View.reloadView: View get() = findViewById(R.id.reloadView)
val View.rvCategory: RecyclerView get() = findViewById(R.id.rvCategory)
val View.swipeRefreshLayout: SwipeRefreshLayout get() = findViewById(R.id.swipeRefreshLayout)
