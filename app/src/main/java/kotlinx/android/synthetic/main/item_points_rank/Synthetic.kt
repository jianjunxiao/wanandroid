package kotlinx.android.synthetic.main.item_points_rank

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.TextView

val Activity.tvName: TextView get() = findViewById(R.id.tvName)
val Fragment.tvName: TextView get() = requireView().findViewById(R.id.tvName)
val View.tvName: TextView get() = findViewById(R.id.tvName)

val Activity.tvNo: TextView get() = findViewById(R.id.tvNo)
val Fragment.tvNo: TextView get() = requireView().findViewById(R.id.tvNo)
val View.tvNo: TextView get() = findViewById(R.id.tvNo)

val Activity.tvPoints: TextView get() = findViewById(R.id.tvPoints)
val Fragment.tvPoints: TextView get() = requireView().findViewById(R.id.tvPoints)
val View.tvPoints: TextView get() = findViewById(R.id.tvPoints)
