package com.xiaojianjun.wanandroid.common

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

open class SimpleFragmentPagerAdapter(
    fm: FragmentManager,
    private val fragments: List<Fragment>,
    private val titles: List<CharSequence>? = null
) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    init {
        require(!(titles != null && titles.size != fragments.size)) {
            "Fragments and titles list size must match!"
        }
    }

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    override fun getPageTitle(position: Int): CharSequence? = titles?.get(position)

    /**
     * 默认为位置，子类需要覆盖默认，才能保证对应位置的fragment可以改变成其他的fragment，
     * 不然同一位置一直都是最初添加的那个fragment，其他的添加不进去
     * {@link FragmentStatePagerAdapter#getItem(int)}
     */
    override fun getItemId(position: Int): Long {
        return fragments[position].hashCode().toLong()
    }
}