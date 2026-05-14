package kotlinx.android.synthetic.main.activity_settings.view

import android.view.View
import com.xiaojianjun.wanandroid.R

import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat

val View.ivBack: ImageView get() = findViewById(R.id.ivBack)
val View.ivTest: ImageView get() = findViewById(R.id.ivTest)
val View.llAboutUs: LinearLayout get() = findViewById(R.id.llAboutUs)
val View.llCheckVersion: LinearLayout get() = findViewById(R.id.llCheckVersion)
val View.llClearCache: LinearLayout get() = findViewById(R.id.llClearCache)
val View.llFontSize: LinearLayout get() = findViewById(R.id.llFontSize)
val View.llLightDark: LinearLayout get() = findViewById(R.id.llLightDark)
val View.scDayNight: SwitchCompat get() = findViewById(R.id.scDayNight)
val View.tvAboutUs: TextView get() = findViewById(R.id.tvAboutUs)
val View.tvCheckVersion: TextView get() = findViewById(R.id.tvCheckVersion)
val View.tvClearCache: TextView get() = findViewById(R.id.tvClearCache)
val View.tvFontSize: TextView get() = findViewById(R.id.tvFontSize)
val View.tvLogout: TextView get() = findViewById(R.id.tvLogout)
