package kotlinx.android.synthetic.main.activity_main

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.FrameLayout
import com.google.android.material.bottomnavigation.BottomNavigationView

val Activity.bottomNavigationView: BottomNavigationView get() = findViewById(R.id.bottomNavigationView)
val Fragment.bottomNavigationView: BottomNavigationView get() = requireView().findViewById(R.id.bottomNavigationView)
val View.bottomNavigationView: BottomNavigationView get() = findViewById(R.id.bottomNavigationView)

val Activity.fl: FrameLayout get() = findViewById(R.id.fl)
val Fragment.fl: FrameLayout get() = requireView().findViewById(R.id.fl)
val View.fl: FrameLayout get() = findViewById(R.id.fl)
