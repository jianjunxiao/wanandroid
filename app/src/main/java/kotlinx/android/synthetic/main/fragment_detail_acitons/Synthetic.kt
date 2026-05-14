package kotlinx.android.synthetic.main.fragment_detail_acitons

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

val Activity.ivCollect: ImageView get() = findViewById(R.id.ivCollect)
val Fragment.ivCollect: ImageView get() = requireView().findViewById(R.id.ivCollect)
val View.ivCollect: ImageView get() = findViewById(R.id.ivCollect)

val Activity.llCollect: LinearLayout get() = findViewById(R.id.llCollect)
val Fragment.llCollect: LinearLayout get() = requireView().findViewById(R.id.llCollect)
val View.llCollect: LinearLayout get() = findViewById(R.id.llCollect)

val Activity.llCopy: LinearLayout get() = findViewById(R.id.llCopy)
val Fragment.llCopy: LinearLayout get() = requireView().findViewById(R.id.llCopy)
val View.llCopy: LinearLayout get() = findViewById(R.id.llCopy)

val Activity.llExplorer: LinearLayout get() = findViewById(R.id.llExplorer)
val Fragment.llExplorer: LinearLayout get() = requireView().findViewById(R.id.llExplorer)
val View.llExplorer: LinearLayout get() = findViewById(R.id.llExplorer)

val Activity.llRefresh: LinearLayout get() = findViewById(R.id.llRefresh)
val Fragment.llRefresh: LinearLayout get() = requireView().findViewById(R.id.llRefresh)
val View.llRefresh: LinearLayout get() = findViewById(R.id.llRefresh)

val Activity.llShare: LinearLayout get() = findViewById(R.id.llShare)
val Fragment.llShare: LinearLayout get() = requireView().findViewById(R.id.llShare)
val View.llShare: LinearLayout get() = findViewById(R.id.llShare)

val Activity.tvCollect: TextView get() = findViewById(R.id.tvCollect)
val Fragment.tvCollect: TextView get() = requireView().findViewById(R.id.tvCollect)
val View.tvCollect: TextView get() = findViewById(R.id.tvCollect)
