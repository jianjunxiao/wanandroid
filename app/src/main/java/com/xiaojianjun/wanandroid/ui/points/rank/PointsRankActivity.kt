package com.xiaojianjun.wanandroid.ui.points.rank

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseVmActivity
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.loadmore.CommonLoadMoreView
import com.xiaojianjun.wanandroid.common.loadmore.LoadMoreStatus
import kotlinx.android.synthetic.main.activity_points_rank.*
import kotlinx.android.synthetic.main.include_reload.*

class PointsRankActivity : BaseVmActivity<PointsRankViewModel>() {

    private lateinit var mAdapter: PointsRankAdapter

    override fun layoutRes() = R.layout.activity_points_rank

    override fun viewModelClass() = PointsRankViewModel::class.java

    override fun initView() {
        mAdapter = PointsRankAdapter().apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener({ mViewModel.loadMoreData() }, recyclerView)
        }
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refreshData() }
        }
        ivBack.setOnClickListener { ActivityHelper.finish(PointsRankActivity::class.java) }
        tvTitle.setOnClickListener { recyclerView.smoothScrollToPosition(0) }
        btnReload.setOnClickListener { mViewModel.refreshData() }
    }

    override fun initData() {
        mViewModel.refreshData()
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            pointsRank.observe(this@PointsRankActivity, {
                mAdapter.setNewData(it)
            })
            refreshStatus.observe(this@PointsRankActivity, {
                swipeRefreshLayout.isRefreshing = it
            })
            loadMoreStatus.observe(this@PointsRankActivity, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> mAdapter.loadMoreComplete()
                    LoadMoreStatus.ERROR -> mAdapter.loadMoreFail()
                    LoadMoreStatus.END -> mAdapter.loadMoreEnd()
                    else -> return@Observer
                }
            })
            reloadStatus.observe(this@PointsRankActivity, {
                reloadView.isVisible = it
            })
        }
    }
}
