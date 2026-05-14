package kotlinx.android.synthetic.main.fragment_home.view

import android.view.View
import com.xiaojianjun.wanandroid.R

import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.tabs.TabLayout

val View.appBarLayout: AppBarLayout get() = findViewById(R.id.appBarLayout)
val View.llSearch: LinearLayout get() = findViewById(R.id.llSearch)
val View.tabLayout: TabLayout get() = findViewById(R.id.tabLayout)
val View.toolbar: Toolbar get() = findViewById(R.id.toolbar)
val View.viewPager: ViewPager get() = findViewById(R.id.viewPager)
