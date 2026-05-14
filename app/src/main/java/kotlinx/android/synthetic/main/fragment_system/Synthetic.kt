package kotlinx.android.synthetic.main.fragment_system

import android.app.Activity
import android.view.View
import androidx.fragment.app.Fragment
import com.xiaojianjun.wanandroid.R

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.ContentLoadingProgressBar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout

val Activity.appBarLayout: AppBarLayout get() = findViewById(R.id.appBarLayout)
val Fragment.appBarLayout: AppBarLayout get() = requireView().findViewById(R.id.appBarLayout)
val View.appBarLayout: AppBarLayout get() = findViewById(R.id.appBarLayout)

val Activity.ivFilter: ImageView get() = findViewById(R.id.ivFilter)
val Fragment.ivFilter: ImageView get() = requireView().findViewById(R.id.ivFilter)
val View.ivFilter: ImageView get() = findViewById(R.id.ivFilter)

val Activity.progressBar: ContentLoadingProgressBar get() = findViewById(R.id.progressBar)
val Fragment.progressBar: ContentLoadingProgressBar get() = requireView().findViewById(R.id.progressBar)
val View.progressBar: ContentLoadingProgressBar get() = findViewById(R.id.progressBar)

val Activity.reloadView: View get() = findViewById(R.id.reloadView)
val Fragment.reloadView: View get() = requireView().findViewById(R.id.reloadView)
val View.reloadView: View get() = findViewById(R.id.reloadView)

val Activity.tabLayout: TabLayout get() = findViewById(R.id.tabLayout)
val Fragment.tabLayout: TabLayout get() = requireView().findViewById(R.id.tabLayout)
val View.tabLayout: TabLayout get() = findViewById(R.id.tabLayout)

val Activity.toolbar: Toolbar get() = findViewById(R.id.toolbar)
val Fragment.toolbar: Toolbar get() = requireView().findViewById(R.id.toolbar)
val View.toolbar: Toolbar get() = findViewById(R.id.toolbar)

val Activity.tvTitle: TextView get() = findViewById(R.id.tvTitle)
val Fragment.tvTitle: TextView get() = requireView().findViewById(R.id.tvTitle)
val View.tvTitle: TextView get() = findViewById(R.id.tvTitle)

val Activity.viewPager: ViewPager get() = findViewById(R.id.viewPager)
val Fragment.viewPager: ViewPager get() = requireView().findViewById(R.id.viewPager)
val View.viewPager: ViewPager get() = findViewById(R.id.viewPager)
