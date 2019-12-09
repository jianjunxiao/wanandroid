package com.xiaojianjun.wanandroid.ui.main.discovery

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.model.bean.HotWord
import kotlinx.android.synthetic.main.item_hot_word.view.*

/**
 * Created by xiaojianjun on 2019-11-16.
 */
class HotWordsAdapter(layouResId: Int = R.layout.item_hot_word) :
    BaseQuickAdapter<HotWord, BaseViewHolder>(layouResId) {
    override fun convert(helper: BaseViewHolder, item: HotWord) {
        helper.itemView.tvName.text = item.name
    }


}