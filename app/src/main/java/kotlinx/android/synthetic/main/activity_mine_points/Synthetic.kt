package kotlinx.android.synthetic.main.activity_mine_points

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

val Activity.ivBack: ImageView get() = findViewById(R.id.ivBack)
val Fragment.ivBack: ImageView get() = requireView().findViewById(R.id.ivBack)
val View.ivBack: ImageView get() = findViewById(R.id.ivBack)

val Activity.ivRank: ImageView get() = findViewById(R.id.ivRank)
val Fragment.ivRank: ImageView get() = requireView().findViewById(R.id.ivRank)
val View.ivRank: ImageView get() = findViewById(R.id.ivRank)

val Activity.recyclerView: RecyclerView get() = findViewById(R.id.recyclerView)
val Fragment.recyclerView: RecyclerView get() = requireView().findViewById(R.id.recyclerView)
val View.recyclerView: RecyclerView get() = findViewById(R.id.recyclerView)

val Activity.reloadView: View get() = findViewById(R.id.reloadView)
val Fragment.reloadView: View get() = requireView().findViewById(R.id.reloadView)
val View.reloadView: View get() = findViewById(R.id.reloadView)

val Activity.swipeRefreshLayout: SwipeRefreshLayout get() = findViewById(R.id.swipeRefreshLayout)
val Fragment.swipeRefreshLayout: SwipeRefreshLayout get() = requireView().findViewById(R.id.swipeRefreshLayout)
val View.swipeRefreshLayout: SwipeRefreshLayout get() = findViewById(R.id.swipeRefreshLayout)
