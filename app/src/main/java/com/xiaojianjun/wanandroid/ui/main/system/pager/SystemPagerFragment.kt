package com.xiaojianjun.wanandroid.ui.main.system.pager

import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseVmFragment
import com.xiaojianjun.wanandroid.common.ScrollToTop
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.loadmore.setLoadMoreStatus
import com.xiaojianjun.wanandroid.ext.dpToPx
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
    private lateinit var mAdapter: SimpleArticleAdapter
    private lateinit var categoryAdapter: CategoryAdapter

    override fun layoutRes() = R.layout.fragment_system_pager

    override fun viewModelClass() = SystemPagerViewModel::class.java

    override fun initView() {
        categoryList = arguments?.getParcelableArrayList(CATEGORY_LIST)!!
        checkedPosition = 0

        initRefresh()
        initCategoryAdapter()
        initArticleAdapter()
        initListeners()
    }

    private fun initRefresh() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener {
                mViewModel.refreshArticleList(categoryList[checkedPosition].id)
            }
        }
    }

    private fun initCategoryAdapter() {
        categoryAdapter = CategoryAdapter(R.layout.item_category_sub).also {
            it.setList(categoryList)
            it.onCheckedListener = { position ->
                checkedPosition = position
                mViewModel.refreshArticleList(categoryList[checkedPosition].id)
            }
            rvCategory.adapter = it
        }
    }

    private fun initArticleAdapter() {
        mAdapter = SimpleArticleAdapter(R.layout.item_article_simple).also {
            it.loadMoreModule.setOnLoadMoreListener {
                mViewModel.loadMoreArticleList(categoryList[checkedPosition].id)
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
            mViewModel.refreshArticleList(categoryList[checkedPosition].id)
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
                ?.scrollToPositionWithOffset(position, 8.dpToPx().toInt())
            mViewModel.refreshArticleList(categoryList[checkedPosition].id)
        }
    }
}