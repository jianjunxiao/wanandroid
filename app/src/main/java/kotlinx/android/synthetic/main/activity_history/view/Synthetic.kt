package kotlinx.android.synthetic.main.activity_history.view

import android.view.View
import com.xiaojianjun.wanandroid.R

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

val View.emptyView: View get() = findViewById(R.id.emptyView)
val View.ivBack: ImageView get() = findViewById(R.id.ivBack)
val View.recyclerView: RecyclerView get() = findViewById(R.id.recyclerView)
