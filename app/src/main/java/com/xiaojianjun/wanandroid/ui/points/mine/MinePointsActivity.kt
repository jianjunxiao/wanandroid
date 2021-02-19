package com.xiaojianjun.wanandroid.ui.points.mine

import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseVmActivity
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.common.loadmore.setLoadMoreStatus
import com.xiaojianjun.wanandroid.ui.points.rank.PointsRankActivity
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
        mAdapter = MinePointsAdapter().also {
            it.loadMoreModule.setOnLoadMoreListener { mViewModel.loadMoreRecord() }
            recyclerView.adapter = it
        }
        swipeRefreshLayout.run {
            setColorSchemeResources(R.color.textColorPrimary)
            setProgressBackgroundColorSchemeResource(R.color.bgColorPrimary)
            setOnRefreshListener { mViewModel.refresh() }
        }
        ivBack.setOnClickListener { ActivityHelper.finish(MinePointsActivity::class.java) }
        ivRank.setOnClickListener { ActivityHelper.startActivity(PointsRankActivity::class.java) }
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
                mAdapter.setList(it)
            })
            refreshStatus.observe(this@MinePointsActivity, {
                swipeRefreshLayout.isRefreshing = it
            })
            loadMoreStatus.observe(this@MinePointsActivity, {
                mAdapter.loadMoreModule.setLoadMoreStatus(it)
            })
            reloadStatus.observe(this@MinePointsActivity, {
                reloadView.isVisible = it
            })
        }
    }
}
