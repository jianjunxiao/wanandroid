package kotlinx.android.synthetic.main.fragment_system_category

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import androidx.recyclerview.widget.RecyclerView

val Activity.recyclerView: RecyclerView get() = findViewById(R.id.recyclerView)
val Fragment.recyclerView: RecyclerView get() = requireView().findViewById(R.id.recyclerView)
val View.recyclerView: RecyclerView get() = findViewById(R.id.recyclerView)
