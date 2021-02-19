package com.xiaojianjun.wanandroid.ui.main.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseFragment
import com.xiaojianjun.wanandroid.common.ScrollToTop
import com.xiaojianjun.wanandroid.common.adapter.SimpleFragmentPagerAdapter
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.ui.main.MainActivity
import com.xiaojianjun.wanandroid.ui.main.home.latest.LatestFragment
import com.xiaojianjun.wanandroid.ui.main.home.plaza.PlazaFragment
import com.xiaojianjun.wanandroid.ui.main.home.popular.PopularFragment
import com.xiaojianjun.wanandroid.ui.main.home.project.ProjectFragment
import com.xiaojianjun.wanandroid.ui.main.home.wechat.WechatFragment
import com.xiaojianjun.wanandroid.ui.search.SearchActivity
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : BaseFragment(), ScrollToTop {

    private lateinit var fragments: List<Fragment>
    private var currentOffset = 0

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun layoutRes() = R.layout.fragment_home

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragments = listOf(
            PopularFragment.newInstance(),
            LatestFragment.newInstance(),
            PlazaFragment.newInstance(),
            ProjectFragment.newInstance(),
            WechatFragment.newInstance()
        )
        val titles = listOf<CharSequence>(
            getString(R.string.popular_articles),
            getString(R.string.latest_project),
            getString(
                R.string.plaza
            ),
            getString(R.string.project_category),
            getString(R.string.wechat_public)
        )
        viewPager.adapter = SimpleFragmentPagerAdapter(
            fm = childFragmentManager,
            fragments = fragments,
            titles = titles
        )
        viewPager.offscreenPageLimit = fragments.size
        tabLayout.setupWithViewPager(viewPager)

        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
            if (activity is MainActivity && this.currentOffset != offset) {
                (activity as MainActivity).animateBottomNavigationView(offset > currentOffset)
                currentOffset = offset
            }
        })
        llSearch.setOnClickListener { ActivityHelper.startActivity(SearchActivity::class.java) }
    }

    override fun scrollToTop() {
        if (!this::fragments.isInitialized) return
        val currentFragment = fragments[viewPager.currentItem]
        if (currentFragment is ScrollToTop && currentFragment.isVisible) {
            currentFragment.scrollToTop()
        }
    }
}