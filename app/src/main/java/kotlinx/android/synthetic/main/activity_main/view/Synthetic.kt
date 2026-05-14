package kotlinx.android.synthetic.main.activity_main.view

import android.view.View
import com.xiaojianjun.wanandroid.R

import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

val View.bottomNavigationView: BottomNavigationView get() = findViewById(R.id.bottomNavigationView)
val View.fl: FrameLayout get() = findViewById(R.id.fl)
