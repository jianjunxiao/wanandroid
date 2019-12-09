package com.xiaojianjun.wanandroid.ui.main.home

import android.annotation.SuppressLint
import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.ext.htmlToSpanned
import com.xiaojianjun.wanandroid.model.bean.Article
import kotlinx.android.synthetic.main.item_article_simple.view.*

/**
 * Created by xiaojianjun on 2019-11-06.
 */
class SimpleArticleAdapter(layoutResId: Int = R.layout.item_article_simple) :
    BaseQuickAdapter<Article, BaseViewHolder>(layoutResId) {

    @SuppressLint("SetTextI18n")
    override fun convert(helper: BaseViewHolder, item: Article) {
        helper.run {
            itemView.run {
                tv_author.text = when {
                    !item.author.isNullOrEmpty() -> {
                        item.author
                    }
                    !item.shareUser.isNullOrEmpty() -> {
                        item.shareUser
                    }
                    else -> context.getString(R.string.anonymous)
                }
                tv_fresh.isVisible = item.fresh
                tv_title.text = item.title.htmlToSpanned()
                tv_time.text = item.niceDate
                iv_collect.isSelected = item.collect
            }
            addOnClickListener(R.id.iv_collect)
        }
    }
}