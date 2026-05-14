package kotlinx.android.synthetic.main.fragment_discovery.view

import android.view.View
import com.xiaojianjun.wanandroid.R

import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.youth.banner.Banner
import com.zhy.view.flowlayout.TagFlowLayout

val View.bannerView: Banner get() = findViewById(R.id.bannerView)
val View.ivAdd: ImageView get() = findViewById(R.id.ivAdd)
val View.ivSearch: ImageView get() = findViewById(R.id.ivSearch)
val View.nestedScollView: NestedScrollView get() = findViewById(R.id.nestedScollView)
val View.reloadView: View get() = findViewById(R.id.reloadView)
val View.rlTitle: RelativeLayout get() = findViewById(R.id.rlTitle)
val View.rvHotWord: RecyclerView get() = findViewById(R.id.rvHotWord)
val View.swipeRefreshLayout: SwipeRefreshLayout get() = findViewById(R.id.swipeRefreshLayout)
val View.tagFlowLayout: TagFlowLayout get() = findViewById(R.id.tagFlowLayout)
val View.tvFrquently: TextView get() = findViewById(R.id.tvFrquently)
val View.tvHotWordTitle: TextView get() = findViewById(R.id.tvHotWordTitle)
