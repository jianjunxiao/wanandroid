package com.xiaojianjun.wanandroid.ui.main.system.pager

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseVmFragment
import com.xiaojianjun.wanandroid.common.ScrollToTop
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.loadmore.CommonLoadMoreView
import com.xiaojianjun.wanandroid.common.loadmore.LoadMoreStatus
import com.xiaojianjun.wanandroid.ext.dpToPxInt
import com.xiaojianjun.wanandroid.model.bean.Category
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.main.home.CategoryAdapter
import com.xiaojianjun.wanandroid.ui.main.home.SimpleArticleAdapter
import kotlinx.android.synthetic.main.fragment_system_pager.*
import kotlinx.android.synthetic.main.include_reload.*

/**
 * Created by xiaojianjun on 2019-11-16.
 */
class SystemPagerFragment : BaseVmFragment<SystemPagerViewModel>(), ScrollToTop {

    companion object {

        private const val CATEGORY_LIST = "CATEGORY_LIST"

        fun newInstance(categoryList: ArrayList<Category>): SystemPagerFragment {
            return SystemPagerFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(CATEGORY_LIST, categoryList)
                }
            }
        }
    }

    private lateinit var categoryList: List<Category>
    var checkedPosition = 0
    private lateinit var mAdapterSimple: SimpleArticleAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    override fun layoutRes() = R.layout.fragment_system_pager

    override fun viewModelClass() = SystemPagerViewModel::class.java

    override fun initView() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener {
                mViewModel.refreshArticleList(categoryList[checkedPosition].id)
            }
        }

        categoryList = arguments?.getParcelableArrayList(CATEGORY_LIST)!!
        checkedPosition = 0
        categoryAdapter = CategoryAdapter(R.layout.item_category_sub).apply {
            bindToRecyclerView(rvCategory)
            setNewData(categoryList)
            onCheckedListener = {
                checkedPosition = it
                mViewModel.refreshArticleList(categoryList[checkedPosition].id)
            }
        }

        mAdapterSimple = SimpleArticleAdapter(R.layout.item_article_simple).apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener({
                mViewModel.loadMoreArticleList(categoryList[checkedPosition].id)
            }, recyclerView)
            setOnItemClickListener { _, _, position ->
                val article = mAdapterSimple.data[position]
                ActivityHelper.start(
                    DetailActivity::class.java,
                    mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            setOnItemChildClickListener { _, view, position ->
                val article = mAdapterSimple.data[position]
                if (view.id == R.id.iv_collect && checkLogin()) {
                    view.isSelected = !view.isSelected
                    if (article.collect) {
                        mViewModel.uncollect(article.id)
                    } else {
                        mViewModel.collect(article.id)
                    }
                }
            }
        }
        btnReload.setOnClickListener {
            mViewModel.refreshArticleList(categoryList[checkedPosition].id)
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            articleList.observe(viewLifecycleOwner, {
                mAdapterSimple.setNewData(it)
            })
            refreshStatus.observe(viewLifecycleOwner, {
                swipeRefreshLayout.isRefreshing = it
            })
            loadMoreStatus.observe(viewLifecycleOwner, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> mAdapterSimple.loadMoreComplete()
                    LoadMoreStatus.ERROR -> mAdapterSimple.loadMoreFail()
                    LoadMoreStatus.END -> mAdapterSimple.loadMoreEnd()
                    else -> return@Observer
                }
            })
            reloadStatus.observe(viewLifecycleOwner, {
                reloadView.isVisible = it
            })
        }
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner, {
            mViewModel.updateListCollectState()
        })
        Bus.observe<Pair<Int, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner, {
            mViewModel.updateItemCollectState(it)
        })
    }

    override fun lazyLoadData() {
        mViewModel.refreshArticleList(categoryList[checkedPosition].id)
    }

    override fun scrollToTop() {
        recyclerView?.smoothScrollToPosition(0)
    }

    fun check(position: Int) {
        if (position != checkedPosition) {
            checkedPosition = position
            categoryAdapter.check(position)
            (rvCategory.layoutManager as? LinearLayoutManager)
                ?.scrollToPositionWithOffset(position, 8f.dpToPxInt())
            mViewModel.refreshArticleList(categoryList[checkedPosition].id)
        }
    }
}