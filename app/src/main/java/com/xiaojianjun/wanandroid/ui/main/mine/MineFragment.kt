package com.xiaojianjun.wanandroid.ui.main.mine

import android.annotation.SuppressLint
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.ui.base.BaseVmFragment
import com.xiaojianjun.wanandroid.ui.collection.CollectionActivity
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity.Companion.PARAM_ARTICLE
import com.xiaojianjun.wanandroid.ui.history.HistoryActivity
import com.xiaojianjun.wanandroid.ui.opensource.OpenSourceActivity
import com.xiaojianjun.wanandroid.ui.points.mine.MinePointsActivity
import com.xiaojianjun.wanandroid.ui.points.rank.PointsRankActivity
import com.xiaojianjun.wanandroid.ui.settings.SettingsActivity
import com.xiaojianjun.wanandroid.ui.shared.SharedActivity
import com.xiaojianjun.wanandroid.util.core.ActivityManager
import com.xiaojianjun.wanandroid.util.core.bus.Bus
import com.xiaojianjun.wanandroid.util.core.bus.USER_LOGIN_STATE_CHANGED
import kotlinx.android.synthetic.main.fragment_mine.*

class MineFragment : BaseVmFragment<MineViewModel>() {

    companion object {
        fun newInstance() = MineFragment()
    }

    override fun layoutRes() = R.layout.fragment_mine

    override fun viewModelClass() = MineViewModel::class.java

    override fun initView() {
        clHeader.setOnClickListener {
            checkLogin { /*上传头像，暂不支持*/ }
        }
        llMyPoints.setOnClickListener {
            checkLogin { ActivityManager.start(MinePointsActivity::class.java) }
        }
        llPointsRank.setOnClickListener {
            ActivityManager.start(PointsRankActivity::class.java)
        }
        llMyShare.setOnClickListener {
            checkLogin { ActivityManager.start(SharedActivity::class.java) }
        }
        llMyCollect.setOnClickListener {
            checkLogin { ActivityManager.start(CollectionActivity::class.java) }
        }
        llHistory.setOnClickListener {
            ActivityManager.start(HistoryActivity::class.java)
        }
        llAboutAuthor.setOnClickListener {
            ActivityManager.start(
                DetailActivity::class.java,
                mapOf(
                    PARAM_ARTICLE to Article(
                        title = getString(R.string.my_about_author),
                        link = "https://github.com/xiaoyanger0825"
                    )
                )
            )
        }
        llOpenSource.setOnClickListener {
            ActivityManager.start(OpenSourceActivity::class.java)
        }
        llSetting.setOnClickListener {
            ActivityManager.start(SettingsActivity::class.java)
        }

        updateUi()
    }

    override fun observe() {
        super.observe()
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner) {
           updateUi()
        }
    }


    @SuppressLint("SetTextI18n")
    private fun updateUi() {
        val isLogin = isLogin()
        val userInfo = UserInfoStore.getUserInfo()
        tvLoginRegister.isGone = isLogin
        tvNickName.isVisible = isLogin
        tvId.isVisible = isLogin
        if (isLogin && userInfo != null) {
            tvNickName.text = userInfo.nickname
            tvId.text = "ID: ${userInfo.id}"
        }
    }
}
