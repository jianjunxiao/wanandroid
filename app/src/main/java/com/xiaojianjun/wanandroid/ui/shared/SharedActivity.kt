package com.xiaojianjun.wanandroid.ui.shared

import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseVmActivity
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_COLLECT_UPDATED
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.loadmore.CommonLoadMoreView
import com.xiaojianjun.wanandroid.common.loadmore.LoadMoreStatus
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.main.home.ArticleAdapter
import com.xiaojianjun.wanandroid.ui.share.ShareActivity
import kotlinx.android.synthetic.main.activity_shared.*
import kotlinx.android.synthetic.main.include_reload.*

class SharedActivity : BaseVmActivity<SharedViewModel>() {

    private lateinit var mAdapter: ArticleAdapter

    override fun layoutRes() = R.layout.activity_shared

    override fun viewModelClass() = SharedViewModel::class.java

    override fun initView() {
        mAdapter = ArticleAdapter().apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(recyclerView)
            setOnItemClickListener { _, _, position ->
                val article = data[position]
                ActivityHelper.start(
                    DetailActivity::class.java, mapOf(DetailActivity.PARAM_ARTICLE to article)
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
            setOnItemLongClickListener { _, _, position ->
                AlertDialog.Builder(this@SharedActivity)
                    .setMessage(R.string.confirm_delete_shared)
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .setPositiveButton(R.string.confirm) { _, _ ->
                        mViewModel.deleteShared(data[position].id)
                        mAdapter.remove(position)
                        this@SharedActivity.emptyView.isVisible = data.isEmpty()
                    }.show()
                true
            }
            setOnLoadMoreListener({ mViewModel.loadMoreArticleList() }, recyclerView)
        }
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refreshArticleList() }
        }
        btnReload.setOnClickListener {
            mViewModel.refreshArticleList()
        }
        ivAdd.setOnClickListener { ActivityHelper.start(ShareActivity::class.java) }
        ivBack.setOnClickListener { ActivityHelper.finish(SharedActivity::class.java) }
    }

    override fun initData() {
        mViewModel.refreshArticleList()
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            articleList.observe(this@SharedActivity, {
                mAdapter.setNewData(it)
            })
            refreshStatus.observe(this@SharedActivity, {
                swipeRefreshLayout.isRefreshing = it
            })
            emptyStatus.observe(this@SharedActivity, {
                emptyView.isVisible = it
            })
            loadMoreStatus.observe(this@SharedActivity, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> mAdapter.loadMoreComplete()
                    LoadMoreStatus.ERROR -> mAdapter.loadMoreFail()
                    LoadMoreStatus.END -> mAdapter.loadMoreEnd()
                    else -> return@Observer
                }
            })
            reloadStatus.observe(this@SharedActivity, {
                reloadView.isVisible = it
            })
        }
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, this, {
            mViewModel.updateListCollectState()
        })
        Bus.observe<Pair<Long, Boolean>>(USER_COLLECT_UPDATED, this, {
            mViewModel.updateItemCollectState(it)
        })
    }
}
