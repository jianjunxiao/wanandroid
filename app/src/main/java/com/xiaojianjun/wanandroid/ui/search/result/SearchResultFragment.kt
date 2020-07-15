package com.xiaojianjun.wanandroid.ui.search.result

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.common.loadmore.CommonLoadMoreView
import com.xiaojianjun.wanandroid.common.loadmore.LoadMoreStatus
import com.xiaojianjun.wanandroid.base.BaseVmFragment
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.main.home.ArticleAdapter
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
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
        searchResultAdapter = ArticleAdapter().apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(recyclerView)
            setOnItemClickListener { _, _, position ->
                val article = data[position]
                ActivityHelper.start(
                    DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            setOnItemChildClickListener { _, view, position ->
                val article = data[position]
                if (view.id == R.id.iv_collect && checkLogin()) {
                    view.isSelected = !view.isSelected
                    if (article.collect) {
                        mViewModel.uncollect(article.id)
                    } else {
                        mViewModel.collect(article.id)
                    }
                }
            }
            setOnLoadMoreListener({ mViewModel.loadMore() }, recyclerView)
        }
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.search() }
        }
        btnReload.setOnClickListener {
            mViewModel.search()
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            articleList.observe(viewLifecycleOwner, Observer {
                searchResultAdapter.setNewData(it)
            })
            refreshStatus.observe(viewLifecycleOwner, Observer {
                swipeRefreshLayout.isRefreshing = it
            })
            emptyStatus.observe(viewLifecycleOwner, Observer {
                emptyView.isVisible = it
            })
            loadMoreStatus.observe(viewLifecycleOwner, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> searchResultAdapter.loadMoreComplete()
                    LoadMoreStatus.ERROR -> searchResultAdapter.loadMoreFail()
                    LoadMoreStatus.END -> searchResultAdapter.loadMoreEnd()
                    else -> return@Observer
                }
            })
            reloadStatus.observe(viewLifecycleOwner, Observer {
                reloadView.isVisible = it
            })
        }
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner) {
            mViewModel.updateListCollectState()
        }
        Bus.observe<Pair<Int, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner) {
            mViewModel.updateItemCollectState(it)
        }
    }

    fun doSearch(searchWords: String) {
        mViewModel.search(searchWords)
    }
}