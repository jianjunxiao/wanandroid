package kotlinx.android.synthetic.main.activity_mine_points.view

import android.view.View
import com.xiaojianjun.wanandroid.R

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

val View.ivBack: ImageView get() = findViewById(R.id.ivBack)
val View.ivRank: ImageView get() = findViewById(R.id.ivRank)
val View.recyclerView: RecyclerView get() = findViewById(R.id.recyclerView)
val View.reloadView: View get() = findViewById(R.id.reloadView)
val View.swipeRefreshLayout: SwipeRefreshLayout get() = findViewById(R.id.swipeRefreshLayout)
