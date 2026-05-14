package kotlinx.android.synthetic.main.fragment_project

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

val Activity.recyclerView: RecyclerView get() = findViewById(R.id.recyclerView)
val Fragment.recyclerView: RecyclerView get() = requireView().findViewById(R.id.recyclerView)
val View.recyclerView: RecyclerView get() = findViewById(R.id.recyclerView)

val Activity.reloadListView: View get() = findViewById(R.id.reloadListView)
val Fragment.reloadListView: View get() = requireView().findViewById(R.id.reloadListView)
val View.reloadListView: View get() = findViewById(R.id.reloadListView)

val Activity.reloadView: View get() = findViewById(R.id.reloadView)
val Fragment.reloadView: View get() = requireView().findViewById(R.id.reloadView)
val View.reloadView: View get() = findViewById(R.id.reloadView)

val Activity.rvCategory: RecyclerView get() = findViewById(R.id.rvCategory)
val Fragment.rvCategory: RecyclerView get() = requireView().findViewById(R.id.rvCategory)
val View.rvCategory: RecyclerView get() = findViewById(R.id.rvCategory)

val Activity.swipeRefreshLayout: SwipeRefreshLayout get() = findViewById(R.id.swipeRefreshLayout)
val Fragment.swipeRefreshLayout: SwipeRefreshLayout get() = requireView().findViewById(R.id.swipeRefreshLayout)
val View.swipeRefreshLayout: SwipeRefreshLayout get() = findViewById(R.id.swipeRefreshLayout)
