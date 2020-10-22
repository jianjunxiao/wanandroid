package com.xiaojianjun.wanandroid.ui.points.rank

import android.annotation.SuppressLint
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.common.loadmore.BaseLoadMoreAdapter
import com.xiaojianjun.wanandroid.model.bean.PointRank
import kotlinx.android.synthetic.main.item_points_rank.view.*

/**
 * Created by xiaojianjun on 2019-12-02.
 */
@SuppressLint("SetTextI18n")
class PointsRankAdapter : BaseLoadMoreAdapter<PointRank, BaseViewHolder>(R.layout.item_points_rank) {
    override fun convert(holder: BaseViewHolder, item: PointRank) {
        holder.itemView.run {
            tvNo.text = "${holder.adapterPosition + 1}"
            tvName.text = item.username
            tvPoints.text = item.coinCount.toString()
        }
    }
}