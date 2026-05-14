package kotlinx.android.synthetic.main.activity_settings

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat

val Activity.ivBack: ImageView get() = findViewById(R.id.ivBack)
val Fragment.ivBack: ImageView get() = requireView().findViewById(R.id.ivBack)
val View.ivBack: ImageView get() = findViewById(R.id.ivBack)

val Activity.ivTest: ImageView get() = findViewById(R.id.ivTest)
val Fragment.ivTest: ImageView get() = requireView().findViewById(R.id.ivTest)
val View.ivTest: ImageView get() = findViewById(R.id.ivTest)

val Activity.llAboutUs: LinearLayout get() = findViewById(R.id.llAboutUs)
val Fragment.llAboutUs: LinearLayout get() = requireView().findViewById(R.id.llAboutUs)
val View.llAboutUs: LinearLayout get() = findViewById(R.id.llAboutUs)

val Activity.llCheckVersion: LinearLayout get() = findViewById(R.id.llCheckVersion)
val Fragment.llCheckVersion: LinearLayout get() = requireView().findViewById(R.id.llCheckVersion)
val View.llCheckVersion: LinearLayout get() = findViewById(R.id.llCheckVersion)

val Activity.llClearCache: LinearLayout get() = findViewById(R.id.llClearCache)
val Fragment.llClearCache: LinearLayout get() = requireView().findViewById(R.id.llClearCache)
val View.llClearCache: LinearLayout get() = findViewById(R.id.llClearCache)

val Activity.llFontSize: LinearLayout get() = findViewById(R.id.llFontSize)
val Fragment.llFontSize: LinearLayout get() = requireView().findViewById(R.id.llFontSize)
val View.llFontSize: LinearLayout get() = findViewById(R.id.llFontSize)

val Activity.llLightDark: LinearLayout get() = findViewById(R.id.llLightDark)
val Fragment.llLightDark: LinearLayout get() = requireView().findViewById(R.id.llLightDark)
val View.llLightDark: LinearLayout get() = findViewById(R.id.llLightDark)

val Activity.scDayNight: SwitchCompat get() = findViewById(R.id.scDayNight)
val Fragment.scDayNight: SwitchCompat get() = requireView().findViewById(R.id.scDayNight)
val View.scDayNight: SwitchCompat get() = findViewById(R.id.scDayNight)

val Activity.tvAboutUs: TextView get() = findViewById(R.id.tvAboutUs)
val Fragment.tvAboutUs: TextView get() = requireView().findViewById(R.id.tvAboutUs)
val View.tvAboutUs: TextView get() = findViewById(R.id.tvAboutUs)

val Activity.tvCheckVersion: TextView get() = findViewById(R.id.tvCheckVersion)
val Fragment.tvCheckVersion: TextView get() = requireView().findViewById(R.id.tvCheckVersion)
val View.tvCheckVersion: TextView get() = findViewById(R.id.tvCheckVersion)

val Activity.tvClearCache: TextView get() = findViewById(R.id.tvClearCache)
val Fragment.tvClearCache: TextView get() = requireView().findViewById(R.id.tvClearCache)
val View.tvClearCache: TextView get() = findViewById(R.id.tvClearCache)

val Activity.tvFontSize: TextView get() = findViewById(R.id.tvFontSize)
val Fragment.tvFontSize: TextView get() = requireView().findViewById(R.id.tvFontSize)
val View.tvFontSize: TextView get() = findViewById(R.id.tvFontSize)

val Activity.tvLogout: TextView get() = findViewById(R.id.tvLogout)
val Fragment.tvLogout: TextView get() = requireView().findViewById(R.id.tvLogout)
val View.tvLogout: TextView get() = findViewById(R.id.tvLogout)
