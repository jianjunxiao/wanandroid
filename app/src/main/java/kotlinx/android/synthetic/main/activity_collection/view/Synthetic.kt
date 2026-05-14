package kotlinx.android.synthetic.main.activity_collection.view

import android.view.View
import com.xiaojianjun.wanandroid.R

import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

val View.emptyView: View get() = findViewById(R.id.emptyView)
val View.ivBack: ImageView get() = findViewById(R.id.ivBack)
val View.recyclerView: RecyclerView get() = findViewById(R.id.recyclerView)
val View.reloadView: View get() = findViewById(R.id.reloadView)
val View.swipeRefreshLayout: SwipeRefreshLayout get() = findViewById(R.id.swipeRefreshLayout)
val View.tvTitle: TextView get() = findViewById(R.id.tvTitle)
