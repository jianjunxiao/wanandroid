package com.xiaojianjun.wanandroid.ui.main.navigation

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.bean.Navigation
import kotlinx.android.synthetic.main.item_navigation.view.*

/**
 * Created by xiaojianjun on 2019-11-15.
 */
class NavigationAdapter(layoutResId: Int = R.layout.item_navigation) :
    BaseQuickAdapter<Navigation, BaseViewHolder>(layoutResId) {

    var onItemTagClickListener: ((article: Article) -> Unit)? = null

    override fun convert(holder: BaseViewHolder, item: Navigation) {
        holder.itemView.run {
            title.text = item.name
            tagFlawLayout.adapter = ItemTagAdapter(item.articles)
            tagFlawLayout.setOnTagClickListener { _, position, _ ->
                onItemTagClickListener?.invoke(item.articles[position])
                true
            }
        }
    }
}