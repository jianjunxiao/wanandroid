package com.xiaojianjun.wanandroid.ui.search.result

import androidx.core.view.isVisible
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseVmFragment
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.loadmore.setLoadMoreStatus
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.main.home.ArticleAdapter
import kotlinx.android.synthetic.main.fragment_search_result.*
import kotlinx.android.synthetic.main.include_reload.*

/**
 * Created by xiaojianjun on 2019-11-29.
 */
class SearchResultFragment : BaseVmFragment<SearchResultViewModel>() {

    companion object {
        fun newInstance(): SearchResultFragment {
            return SearchResultFragment()
        }
    }

    private lateinit var searchResultAdapter: ArticleAdapter

    override fun layoutRes() = R.layout.fragment_search_result

    override fun viewModelClass() = SearchResultViewModel::class.java

    override fun initView() {
        initAdapter()
        initRefresh()
        initListeners()
    }

    private fun initRefresh() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.search() }
        }
    }

    private fun initAdapter() {
        searchResultAdapter = ArticleAdapter().also {
            it.loadMoreModule.setOnLoadMoreListener {
                mViewModel.loadMore()
            }
            it.setOnItemClickListener { _, _, position ->
                val article = it.data[position]
                ActivityHelper.startActivity(
                    DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article)
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
            mViewModel.search()
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.articleList.observe(viewLifecycleOwner, {
            searchResultAdapter.setList(it)
        })
        mViewModel.refreshStatus.observe(viewLifecycleOwner, {
            swipeRefreshLayout.isRefreshing = it
        })
        mViewModel.emptyStatus.observe(viewLifecycleOwner, {
            emptyView.isVisible = it
        })
        mViewModel.loadMoreStatus.observe(viewLifecycleOwner, {
            searchResultAdapter.loadMoreModule.setLoadMoreStatus(it)
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

    fun doSearch(searchWords: String) {
        mViewModel.search(searchWords)
    }
}