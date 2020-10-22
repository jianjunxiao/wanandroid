package com.xiaojianjun.wanandroid.ui.collection

import androidx.core.view.isVisible
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseVmActivity
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.loadmore.setLoadMoreStatus
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.main.home.ArticleAdapter
import kotlinx.android.synthetic.main.activity_collection.*
import kotlinx.android.synthetic.main.include_reload.*

class CollectionActivity : BaseVmActivity<CollectionViewModel>() {

    private lateinit var mAdater: ArticleAdapter

    override fun layoutRes() = R.layout.activity_collection

    override fun viewModelClass() = CollectionViewModel::class.java

    override fun initView() {
        initAdapter()
        initRefresh()
        initListeners()
    }

    private fun initAdapter() {
        mAdater = ArticleAdapter().also {
            it.setOnItemClickListener { _, _, position ->
                val article = it.data[position]
                ActivityHelper.start(
                    DetailActivity::class.java, mapOf(DetailActivity.PARAM_ARTICLE to article)
                )
            }
            it.setOnItemChildClickListener { _, view, position ->
                val article = it.data[position]
                if (view.id == R.id.iv_collect) {
                    mViewModel.uncollect(article.originId)
                    removeItem(position)
                }
            }
            it.loadMoreModule.setOnLoadMoreListener {
                mViewModel.loadMore()
            }
            recyclerView.adapter = it
        }
    }

    private fun initRefresh() {
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refresh() }
        }
    }

    private fun initListeners() {
        btnReload.setOnClickListener {
            mViewModel.refresh()
        }
        ivBack.setOnClickListener {
            ActivityHelper.finish(CollectionActivity::class.java)
        }
    }

    override fun initData() {
        mViewModel.refresh()
    }

    override fun observe() {
        super.observe()
        mViewModel.articleList.observe(this, {
            mAdater.setList(it)
        })
        mViewModel.refreshStatus.observe(this, {
            swipeRefreshLayout.isRefreshing = it
        })
        mViewModel.emptyStatus.observe(this, {
            emptyView.isVisible = it
        })
        mViewModel.loadMoreStatus.observe(this, {
            mAdater.loadMoreModule.setLoadMoreStatus(it)
        })
        mViewModel.reloadStatus.observe(this, {
            reloadView.isVisible = it
        })
        Bus.observe<Pair<Long, Boolean>>(USER_COLLECT_UPDATED, this, {
            onCollectUpdated(it.first, it.second)
        })
    }

    private fun onCollectUpdated(id: Long, collect: Boolean) {
        if (collect) {
            mViewModel.refresh()
        } else {
            val position = mAdater.data.indexOfFirst { it.originId == id }
            if (position != -1) {
                removeItem(position)
            }
        }
    }

    private fun removeItem(position: Int) {
        mAdater.removeAt(position)
        emptyView.isVisible = mAdater.data.isEmpty()
    }

}
