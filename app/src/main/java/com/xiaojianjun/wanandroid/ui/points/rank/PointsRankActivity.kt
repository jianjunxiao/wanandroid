package com.xiaojianjun.wanandroid.ui.points.rank

import androidx.core.view.isVisible
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseVmActivity
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.loadmore.setLoadMoreStatus
import kotlinx.android.synthetic.main.activity_points_rank.*
import kotlinx.android.synthetic.main.include_reload.*

class PointsRankActivity : BaseVmActivity<PointsRankViewModel>() {

    private lateinit var mAdapter: PointsRankAdapter

    override fun layoutRes() = R.layout.activity_points_rank

    override fun viewModelClass() = PointsRankViewModel::class.java

    override fun initView() {
        mAdapter = PointsRankAdapter().also {
            it.loadMoreModule.setOnLoadMoreListener { mViewModel.loadMoreData() }
            recyclerView.adapter = it
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
                mAdapter.setList(it)
            })
            refreshStatus.observe(this@PointsRankActivity, {
                swipeRefreshLayout.isRefreshing = it
            })
            loadMoreStatus.observe(this@PointsRankActivity, {
                mAdapter.loadMoreModule.setLoadMoreStatus(it)
            })
            reloadStatus.observe(this@PointsRankActivity, {
                reloadView.isVisible = it
            })
        }
    }
}
