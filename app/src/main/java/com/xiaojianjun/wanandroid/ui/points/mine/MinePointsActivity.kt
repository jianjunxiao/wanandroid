package com.xiaojianjun.wanandroid.ui.points.mine

import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.common.loadmore.CommonLoadMoreView
import com.xiaojianjun.wanandroid.common.loadmore.LoadMoreStatus
import com.xiaojianjun.wanandroid.base.BaseVmActivity
import com.xiaojianjun.wanandroid.ui.points.rank.PointsRankActivity
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import kotlinx.android.synthetic.main.activity_mine_points.*
import kotlinx.android.synthetic.main.header_mine_points.view.*
import kotlinx.android.synthetic.main.include_reload.*

class MinePointsActivity : BaseVmActivity<MinePointsViewModel>() {

    private lateinit var mAdapter: MinePointsAdapter
    private lateinit var mHeaderView: View

    override fun layoutRes() = R.layout.activity_mine_points

    override fun viewModelClass() = MinePointsViewModel::class.java

    override fun initView() {
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.header_mine_points, null)
        mAdapter = MinePointsAdapter().apply {
            setLoadMoreView(CommonLoadMoreView())
            bindToRecyclerView(recyclerView)
            setOnLoadMoreListener({ mViewModel.loadMoreRecord() }, recyclerView)
        }
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refresh() }
        }
        ivBack.setOnClickListener { ActivityHelper.finish(MinePointsActivity::class.java) }
        ivRank.setOnClickListener { ActivityHelper.start(PointsRankActivity::class.java) }
        btnReload.setOnClickListener { mViewModel.refresh() }
    }

    override fun initData() {
        mViewModel.refresh()
    }

    override fun observe() {
        super.observe()
        mViewModel.run {
            totalPoints.observe(this@MinePointsActivity, {
                if (mAdapter.headerLayoutCount == 0) {
                    mAdapter.setHeaderView(mHeaderView)
                }
                mHeaderView.tvTotalPoints.text = it.coinCount.toString()
                mHeaderView.tvLevelRank.text = getString(R.string.level_rank, it.level, it.rank)

            })
            pointsList.observe(this@MinePointsActivity, {
                mAdapter.setNewData(it)
            })
            refreshStatus.observe(this@MinePointsActivity, {
                swipeRefreshLayout.isRefreshing = it
            })
            loadMoreStatus.observe(this@MinePointsActivity, Observer {
                when (it) {
                    LoadMoreStatus.COMPLETED -> mAdapter.loadMoreComplete()
                    LoadMoreStatus.ERROR -> mAdapter.loadMoreFail()
                    LoadMoreStatus.END -> mAdapter.loadMoreEnd()
                    else -> return@Observer
                }
            })
            reloadStatus.observe(this@MinePointsActivity, {
                reloadView.isVisible = it
            })
        }
    }
}
