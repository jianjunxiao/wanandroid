package kotlinx.android.synthetic.main.fragment_system.view

import android.view.View
import com.xiaojianjun.wanandroid.R

import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.ContentLoadingProgressBar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout

val View.appBarLayout: AppBarLayout get() = findViewById(R.id.appBarLayout)
val View.ivFilter: ImageView get() = findViewById(R.id.ivFilter)
val View.progressBar: ContentLoadingProgressBar get() = findViewById(R.id.progressBar)
val View.reloadView: View get() = findViewById(R.id.reloadView)
val View.tabLayout: TabLayout get() = findViewById(R.id.tabLayout)
val View.toolbar: Toolbar get() = findViewById(R.id.toolbar)
val View.tvTitle: TextView get() = findViewById(R.id.tvTitle)
val View.viewPager: ViewPager get() = findViewById(R.id.viewPager)
