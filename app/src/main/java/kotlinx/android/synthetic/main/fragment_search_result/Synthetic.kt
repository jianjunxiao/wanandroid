package kotlinx.android.synthetic.main.fragment_search_result

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

val Activity.emptyView: View get() = findViewById(R.id.emptyView)
val Fragment.emptyView: View get() = requireView().findViewById(R.id.emptyView)
val View.emptyView: View get() = findViewById(R.id.emptyView)

val Activity.recyclerView: RecyclerView get() = findViewById(R.id.recyclerView)
val Fragment.recyclerView: RecyclerView get() = requireView().findViewById(R.id.recyclerView)
val View.recyclerView: RecyclerView get() = findViewById(R.id.recyclerView)

val Activity.reloadView: View get() = findViewById(R.id.reloadView)
val Fragment.reloadView: View get() = requireView().findViewById(R.id.reloadView)
val View.reloadView: View get() = findViewById(R.id.reloadView)

val Activity.swipeRefreshLayout: SwipeRefreshLayout get() = findViewById(R.id.swipeRefreshLayout)
val Fragment.swipeRefreshLayout: SwipeRefreshLayout get() = requireView().findViewById(R.id.swipeRefreshLayout)
val View.swipeRefreshLayout: SwipeRefreshLayout get() = findViewById(R.id.swipeRefreshLayout)
