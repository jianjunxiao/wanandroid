package com.xiaojianjun.wanandroid.ui.points.rank

import android.annotation.SuppressLint
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.model.bean.PointRank
import kotlinx.android.synthetic.main.item_points_rank.view.*

/**
 * Created by xiaojianjun on 2019-12-02.
 */
@SuppressLint("SetTextI18n")
class PointsRankAdapter : BaseQuickAdapter<PointRank, BaseViewHolder>(R.layout.item_points_rank) {
    override fun convert(helper: BaseViewHolder, item: PointRank) {
        helper.itemView.run {
            tvNo.text = "${helper.adapterPosition + 1}"
            tvName.text = item.username
            tvPoints.text = item.coinCount.toString()
        }
    }
}