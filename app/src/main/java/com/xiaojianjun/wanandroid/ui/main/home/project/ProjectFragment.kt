package com.xiaojianjun.wanandroid.ui.main.home.project

import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseVmFragment
import com.xiaojianjun.wanandroid.common.ScrollToTop
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.loadmore.setLoadMoreStatus
import com.xiaojianjun.wanandroid.model.bean.Category
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.main.home.ArticleAdapter
import com.xiaojianjun.wanandroid.ui.main.home.CategoryAdapter
import kotlinx.android.synthetic.main.fragment_project.*
import kotlinx.android.synthetic.main.include_reload.view.*

class ProjectFragment : BaseVmFragment<ProjectViewModel>(),
    ScrollToTop {

    companion object {
        fun newInstance() = ProjectFragment()
    }

    private lateinit var mAdapter: ArticleAdapter
    private lateinit var mCategoryAdapter: CategoryAdapter

    override fun layoutRes() = R.layout.fragment_project

    override fun viewModelClass() = ProjectViewModel::class.java

    override fun initView() {
        initRefresh()
        initCategoryAdapter()
        initArticleAdapter()
        initListeners()
    }

    private fun initRefresh() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refreshProjectList() }
        }
    }

    private fun initCategoryAdapter() {
        mCategoryAdapter = CategoryAdapter(R.layout.item_category_sub).also {
            it.onCheckedListener = { position ->
                mViewModel.refreshProjectList(position)
            }
            rvCategory.adapter = it
        }
    }

    private fun initArticleAdapter() {
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
        reloadListView.btnReload.setOnClickListener {
            mViewModel.refreshProjectList()
        }
        reloadView.btnReload.setOnClickListener {
            mViewModel.getProjectCategory()
        }
    }

    override fun observe() {
        super.observe()
        mViewModel.categories.observe(viewLifecycleOwner, {
            updateCategory(it)
        })
        mViewModel.checkedCategory.observe(viewLifecycleOwner, {
            mCategoryAdapter.check(it)
        })
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
        mViewModel.reloadListStatus.observe(viewLifecycleOwner, {
            reloadListView.isVisible = it
        })
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner, {
            mViewModel.updateListCollectState()
        })
        Bus.observe<Pair<Long, Boolean>>(USER_COLLECT_UPDATED, viewLifecycleOwner, {
            mViewModel.updateItemCollectState(it)
        })
    }

    private fun updateCategory(categoryList: MutableList<Category>) {
        rvCategory.isGone = categoryList.isEmpty()
        mCategoryAdapter.setList(categoryList)
    }

    override fun lazyLoadData() {
        mViewModel.getProjectCategory()
    }

    override fun scrollToTop() {
        recyclerView.smoothScrollToPosition(0)
    }
}
