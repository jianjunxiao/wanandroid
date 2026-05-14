package kotlinx.android.synthetic.main.fragment_profile

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

val Activity.civAratar: ImageView get() = findViewById(R.id.civAratar)
val Fragment.civAratar: ImageView get() = requireView().findViewById(R.id.civAratar)
val View.civAratar: ImageView get() = findViewById(R.id.civAratar)

val Activity.clHeader: ConstraintLayout get() = findViewById(R.id.clHeader)
val Fragment.clHeader: ConstraintLayout get() = requireView().findViewById(R.id.clHeader)
val View.clHeader: ConstraintLayout get() = findViewById(R.id.clHeader)

val Activity.llAboutAuthor: LinearLayout get() = findViewById(R.id.llAboutAuthor)
val Fragment.llAboutAuthor: LinearLayout get() = requireView().findViewById(R.id.llAboutAuthor)
val View.llAboutAuthor: LinearLayout get() = findViewById(R.id.llAboutAuthor)

val Activity.llHistory: LinearLayout get() = findViewById(R.id.llHistory)
val Fragment.llHistory: LinearLayout get() = requireView().findViewById(R.id.llHistory)
val View.llHistory: LinearLayout get() = findViewById(R.id.llHistory)

val Activity.llMyCollect: LinearLayout get() = findViewById(R.id.llMyCollect)
val Fragment.llMyCollect: LinearLayout get() = requireView().findViewById(R.id.llMyCollect)
val View.llMyCollect: LinearLayout get() = findViewById(R.id.llMyCollect)

val Activity.llMyPoints: LinearLayout get() = findViewById(R.id.llMyPoints)
val Fragment.llMyPoints: LinearLayout get() = requireView().findViewById(R.id.llMyPoints)
val View.llMyPoints: LinearLayout get() = findViewById(R.id.llMyPoints)

val Activity.llMyShare: LinearLayout get() = findViewById(R.id.llMyShare)
val Fragment.llMyShare: LinearLayout get() = requireView().findViewById(R.id.llMyShare)
val View.llMyShare: LinearLayout get() = findViewById(R.id.llMyShare)

val Activity.llOpenSource: LinearLayout get() = findViewById(R.id.llOpenSource)
val Fragment.llOpenSource: LinearLayout get() = requireView().findViewById(R.id.llOpenSource)
val View.llOpenSource: LinearLayout get() = findViewById(R.id.llOpenSource)

val Activity.llPointsRank: LinearLayout get() = findViewById(R.id.llPointsRank)
val Fragment.llPointsRank: LinearLayout get() = requireView().findViewById(R.id.llPointsRank)
val View.llPointsRank: LinearLayout get() = findViewById(R.id.llPointsRank)

val Activity.llSetting: LinearLayout get() = findViewById(R.id.llSetting)
val Fragment.llSetting: LinearLayout get() = requireView().findViewById(R.id.llSetting)
val View.llSetting: LinearLayout get() = findViewById(R.id.llSetting)

val Activity.tvId: TextView get() = findViewById(R.id.tvId)
val Fragment.tvId: TextView get() = requireView().findViewById(R.id.tvId)
val View.tvId: TextView get() = findViewById(R.id.tvId)

val Activity.tvLoginRegister: TextView get() = findViewById(R.id.tvLoginRegister)
val Fragment.tvLoginRegister: TextView get() = requireView().findViewById(R.id.tvLoginRegister)
val View.tvLoginRegister: TextView get() = findViewById(R.id.tvLoginRegister)

val Activity.tvNickName: TextView get() = findViewById(R.id.tvNickName)
val Fragment.tvNickName: TextView get() = requireView().findViewById(R.id.tvNickName)
val View.tvNickName: TextView get() = findViewById(R.id.tvNickName)
