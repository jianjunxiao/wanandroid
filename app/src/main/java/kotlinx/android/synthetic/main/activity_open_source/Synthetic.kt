package kotlinx.android.synthetic.main.activity_open_source

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

val Activity.ivBack: ImageView get() = findViewById(R.id.ivBack)
val Fragment.ivBack: ImageView get() = requireView().findViewById(R.id.ivBack)
val View.ivBack: ImageView get() = findViewById(R.id.ivBack)

val Activity.recyclerView: RecyclerView get() = findViewById(R.id.recyclerView)
val Fragment.recyclerView: RecyclerView get() = requireView().findViewById(R.id.recyclerView)
val View.recyclerView: RecyclerView get() = findViewById(R.id.recyclerView)
