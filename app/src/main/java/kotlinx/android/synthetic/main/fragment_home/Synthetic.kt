package kotlinx.android.synthetic.main.fragment_home

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout

val Activity.appBarLayout: AppBarLayout get() = findViewById(R.id.appBarLayout)
val Fragment.appBarLayout: AppBarLayout get() = requireView().findViewById(R.id.appBarLayout)
val View.appBarLayout: AppBarLayout get() = findViewById(R.id.appBarLayout)

val Activity.llSearch: LinearLayout get() = findViewById(R.id.llSearch)
val Fragment.llSearch: LinearLayout get() = requireView().findViewById(R.id.llSearch)
val View.llSearch: LinearLayout get() = findViewById(R.id.llSearch)

val Activity.tabLayout: TabLayout get() = findViewById(R.id.tabLayout)
val Fragment.tabLayout: TabLayout get() = requireView().findViewById(R.id.tabLayout)
val View.tabLayout: TabLayout get() = findViewById(R.id.tabLayout)

val Activity.toolbar: Toolbar get() = findViewById(R.id.toolbar)
val Fragment.toolbar: Toolbar get() = requireView().findViewById(R.id.toolbar)
val View.toolbar: Toolbar get() = findViewById(R.id.toolbar)

val Activity.viewPager: ViewPager get() = findViewById(R.id.viewPager)
val Fragment.viewPager: ViewPager get() = requireView().findViewById(R.id.viewPager)
val View.viewPager: ViewPager get() = findViewById(R.id.viewPager)
