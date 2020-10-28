package com.xiaojianjun.wanandroid.ui.points.mine

import android.annotation.SuppressLint
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.common.loadmore.BaseLoadMoreAdapter
import com.xiaojianjun.wanandroid.ext.toDateTime
import com.xiaojianjun.wanandroid.model.bean.PointRecord
import kotlinx.android.synthetic.main.item_mine_points.view.*

/**
 * Created by xiaojianjun on 2019-12-02.
 */
@SuppressLint("SetTextI18n")
class MinePointsAdapter :
    BaseLoadMoreAdapter<PointRecord, BaseViewHolder>(R.layout.item_mine_points) {
    override fun convert(holder: BaseViewHolder, item: PointRecord) {
        holder.itemView.run {
            tvReason.text = item.reason
            tvTime.text = item.date.toDateTime("YYYY-MM-dd HH:mm:ss")
            tvPoint.text = "+${item.coinCount}"
        }
    }

}