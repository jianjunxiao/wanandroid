package com.xiaojianjun.wanandroid.ui.main.discovery

import android.view.LayoutInflater
import android.view.View
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.model.bean.Frequently
import com.zhy.view.flowlayout.FlowLayout
import com.zhy.view.flowlayout.TagAdapter
import kotlinx.android.synthetic.main.item_nav_tag.view.*

class TagAdapter(private val frequentlyList: List<Frequently>) :
    TagAdapter<Frequently>(frequentlyList) {
    override fun getView(parent: FlowLayout?, position: Int, frequently: Frequently?): View {
        return LayoutInflater.from(parent?.context)
            .inflate(R.layout.item_nav_tag, parent, false)
            .apply {
                tvTag.text = frequentlyList[position].name
            }
    }
}