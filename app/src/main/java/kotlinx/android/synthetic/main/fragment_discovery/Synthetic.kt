package kotlinx.android.synthetic.main.fragment_discovery

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.youth.banner.Banner
import com.zhy.view.flowlayout.TagFlowLayout

val Activity.bannerView: Banner get() = findViewById(R.id.bannerView)
val Fragment.bannerView: Banner get() = requireView().findViewById(R.id.bannerView)
val View.bannerView: Banner get() = findViewById(R.id.bannerView)

val Activity.ivAdd: ImageView get() = findViewById(R.id.ivAdd)
val Fragment.ivAdd: ImageView get() = requireView().findViewById(R.id.ivAdd)
val View.ivAdd: ImageView get() = findViewById(R.id.ivAdd)

val Activity.ivSearch: ImageView get() = findViewById(R.id.ivSearch)
val Fragment.ivSearch: ImageView get() = requireView().findViewById(R.id.ivSearch)
val View.ivSearch: ImageView get() = findViewById(R.id.ivSearch)

val Activity.nestedScollView: NestedScrollView get() = findViewById(R.id.nestedScollView)
val Fragment.nestedScollView: NestedScrollView get() = requireView().findViewById(R.id.nestedScollView)
val View.nestedScollView: NestedScrollView get() = findViewById(R.id.nestedScollView)

val Activity.reloadView: View get() = findViewById(R.id.reloadView)
val Fragment.reloadView: View get() = requireView().findViewById(R.id.reloadView)
val View.reloadView: View get() = findViewById(R.id.reloadView)

val Activity.rlTitle: RelativeLayout get() = findViewById(R.id.rlTitle)
val Fragment.rlTitle: RelativeLayout get() = requireView().findViewById(R.id.rlTitle)
val View.rlTitle: RelativeLayout get() = findViewById(R.id.rlTitle)

val Activity.rvHotWord: RecyclerView get() = findViewById(R.id.rvHotWord)
val Fragment.rvHotWord: RecyclerView get() = requireView().findViewById(R.id.rvHotWord)
val View.rvHotWord: RecyclerView get() = findViewById(R.id.rvHotWord)

val Activity.swipeRefreshLayout: SwipeRefreshLayout get() = findViewById(R.id.swipeRefreshLayout)
val Fragment.swipeRefreshLayout: SwipeRefreshLayout get() = requireView().findViewById(R.id.swipeRefreshLayout)
val View.swipeRefreshLayout: SwipeRefreshLayout get() = findViewById(R.id.swipeRefreshLayout)

val Activity.tagFlowLayout: TagFlowLayout get() = findViewById(R.id.tagFlowLayout)
val Fragment.tagFlowLayout: TagFlowLayout get() = requireView().findViewById(R.id.tagFlowLayout)
val View.tagFlowLayout: TagFlowLayout get() = findViewById(R.id.tagFlowLayout)

val Activity.tvFrquently: TextView get() = findViewById(R.id.tvFrquently)
val Fragment.tvFrquently: TextView get() = requireView().findViewById(R.id.tvFrquently)
val View.tvFrquently: TextView get() = findViewById(R.id.tvFrquently)

val Activity.tvHotWordTitle: TextView get() = findViewById(R.id.tvHotWordTitle)
val Fragment.tvHotWordTitle: TextView get() = requireView().findViewById(R.id.tvHotWordTitle)
val View.tvHotWordTitle: TextView get() = findViewById(R.id.tvHotWordTitle)
