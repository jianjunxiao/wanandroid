package kotlinx.android.synthetic.main.item_mine_points

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.TextView

val Activity.tvPoint: TextView get() = findViewById(R.id.tvPoint)
val Fragment.tvPoint: TextView get() = requireView().findViewById(R.id.tvPoint)
val View.tvPoint: TextView get() = findViewById(R.id.tvPoint)

val Activity.tvReason: TextView get() = findViewById(R.id.tvReason)
val Fragment.tvReason: TextView get() = requireView().findViewById(R.id.tvReason)
val View.tvReason: TextView get() = findViewById(R.id.tvReason)

val Activity.tvTime: TextView get() = findViewById(R.id.tvTime)
val Fragment.tvTime: TextView get() = requireView().findViewById(R.id.tvTime)
val View.tvTime: TextView get() = findViewById(R.id.tvTime)
