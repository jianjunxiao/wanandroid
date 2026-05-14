package kotlinx.android.synthetic.main.fragment_latest.view

import android.view.View
import com.xiaojianjun.wanandroid.R

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

val View.recyclerView: RecyclerView get() = findViewById(R.id.recyclerView)
val View.reloadView: View get() = findViewById(R.id.reloadView)
val View.swipeRefreshLayout: SwipeRefreshLayout get() = findViewById(R.id.swipeRefreshLayout)
