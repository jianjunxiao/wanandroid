package com.xiaojianjun.wanandroid.ui.opensource

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.model.bean.Article
import kotlinx.android.synthetic.main.item_open_source.view.*

/**
 * Created by xiaojianjun on 2019-12-08.
 */
class OpenSourceAdapter : BaseQuickAdapter<Article, BaseViewHolder>(R.layout.item_open_source) {
    override fun convert(helper: BaseViewHolder, item: Article) {
        helper.itemView.run {
            tvTitle.text = item.title
            tvLink.text = item.link
        }
    }
}