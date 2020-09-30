package com.xiaojianjun.wanandroid.ui.main.navigation

import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseVmFragment
import com.xiaojianjun.wanandroid.common.ScrollToTop
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.main.MainActivity
import kotlinx.android.synthetic.main.fragment_navigation.*
import kotlinx.android.synthetic.main.include_reload.*

class NavigationFragment : BaseVmFragment<NavigationViewModel>(), ScrollToTop {

    private lateinit var mAdapter: NavigationAdapter
    private var currentPosition = 0

    companion object {
        fun newInstance() = NavigationFragment()
    }

    override fun layoutRes() = R.layout.fragment_navigation

    override fun viewModelClass() = NavigationViewModel::class.java

    override fun initView() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.getNavigations() }
        }
        mAdapter = NavigationAdapter(R.layout.item_navigation).apply {
            bindToRecyclerView(recyclerView)
            onItemTagClickListener = {
                ActivityHelper.start(
                    DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to Article(title = it.title, link = it.link))
                )
            }
        }
        btnReload.setOnClickListener {
            mViewModel.getNavigations()
        }
        recyclerView.setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
            if (activity is MainActivity && scrollY != oldScrollY) {
                (activity as MainActivity).animateBottomNavigationView(scrollY < oldScrollY)
            }
            if (scrollY < oldScrollY) {
                tvFloatTitle.text = mAdapter.data[currentPosition].name
            }
            val lm = recyclerView.layoutManager as LinearLayoutManager
            val nextView = lm.findViewByPosition(currentPosition + 1)
            if (nextView != null) {
                tvFloatTitle.y = if (nextView.top < tvFloatTitle.measuredHeight) {
                    (nextView.top - tvFloatTitle.measuredHeight).toFloat()
                } else {
                    0f
                }
            }
            currentPosition = lm.findFirstVisibleItemPosition()
            if (scrollY > oldScrollY) {
                tvFloatTitle.text = mAdapter.data[currentPosition].name
            }
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            navigations.observe(viewLifecycleOwner, {
                tvFloatTitle.isGone = it.isEmpty()
                tvFloatTitle.text = it[0].name
                mAdapter.setNewData(it)
            })
            refreshStatus.observe(viewLifecycleOwner, {
                swipeRefreshLayout.isRefreshing = it
            })
            reloadStatus.observe(viewLifecycleOwner, {
                reloadView.isVisible = it
            })
        }
    }

    override fun initData() {
        mViewModel.getNavigations()
    }

    override fun scrollToTop() {
        recyclerView?.smoothScrollToPosition(0)
    }
}
