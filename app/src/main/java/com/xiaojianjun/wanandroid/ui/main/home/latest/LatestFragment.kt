package com.xiaojianjun.wanandroid.ui.main.home.latest

import androidx.core.view.isVisible
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseVmFragment
import com.xiaojianjun.wanandroid.common.ScrollToTop
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.loadmore.setLoadMoreStatus
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.main.home.ArticleAdapter
import kotlinx.android.synthetic.main.fragment_latest.*
import kotlinx.android.synthetic.main.include_reload.*

class LatestFragment : BaseVmFragment<LatestViewModel>(), ScrollToTop {

    companion object {
        fun newInstance() = LatestFragment()
    }

    private lateinit var mAdapter: ArticleAdapter

    override fun layoutRes() = R.layout.fragment_latest

    override fun viewModelClass() = LatestViewModel::class.java

    override fun initView() {
        initRefresh()
        initAdapter()
        initListeners()
    }

    private fun initRefresh() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refreshProjectList() }
        }
    }

    private fun initAdapter() {
        mAdapter = ArticleAdapter(R.layout.item_article).also {
            it.loadMoreModule.setOnLoadMoreListener {
                mViewModel.loadMoreProjectList()
            }
            it.setOnItemClickListener { _, _, position ->
                val article = it.data[position]
                ActivityHelper.startActivity(
                    DetailActivity::class.java, mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            it.setOnItemChildClickListener { _, view, position ->
                val article = it.data[position]
                if (view.id == R.id.iv_collect && checkLogin()) {
                    view.isSelected = !view.isSelected
                    if (article.collect) {
                        mViewModel.uncollect(article.id)
                    } else {
                        mViewModel.collect(article.id)
                    }
                }
            }
            recyclerView.adapter = it
        }
    }

    private fun initListeners() {
        btnReload.setOnClickListener {
            mViewModel.refreshProjectList()
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.articleList.observe(viewLifecycleOwner, {
            mAdapter.setList(it)
        })
        mViewModel.refreshStatus.observe(viewLifecycleOwner, {
            swipeRefreshLayout.isRefreshing = it
        })
        mViewModel.loadMoreStatus.observe(viewLifecycleOwner, {
            mAdapter.loadMoreModule.setLoadMoreStatus(it)
        })
        mViewModel.reloadStatus.observe(viewLifecycleOwner, {
            reloadView.isVisible = it
        })
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner, {
            mViewModel.updateListCollectState()
        })
        Bus.observe<Pair<Long, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner, {
            mViewModel.updateItemCollectState(it)
        })
    }

    override fun lazyLoadData() {
        mViewModel.refreshProjectList()
    }

    override fun scrollToTop() {
        recyclerView.smoothScrollToPosition(0)
    }
}
