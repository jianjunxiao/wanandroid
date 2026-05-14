package kotlinx.android.synthetic.main.header_mine_points

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.TextView

val Activity.tvLevelRank: TextView get() = findViewById(R.id.tvLevelRank)
val Fragment.tvLevelRank: TextView get() = requireView().findViewById(R.id.tvLevelRank)
val View.tvLevelRank: TextView get() = findViewById(R.id.tvLevelRank)

val Activity.tvTotalPoints: TextView get() = findViewById(R.id.tvTotalPoints)
val Fragment.tvTotalPoints: TextView get() = requireView().findViewById(R.id.tvTotalPoints)
val View.tvTotalPoints: TextView get() = findViewById(R.id.tvTotalPoints)
