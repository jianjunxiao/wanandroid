package com.xiaojianjun.wanandroid.ui.main.profile

import android.annotation.SuppressLint
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseVmFragment
import com.xiaojianjun.wanandroid.common.bus.Bus
import com.xiaojianjun.wanandroid.common.bus.USER_LOGIN_STATE_CHANGED
import com.xiaojianjun.wanandroid.common.core.ActivityHelper
import com.xiaojianjun.wanandroid.model.bean.Article
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import com.xiaojianjun.wanandroid.model.store.isLogin
import com.xiaojianjun.wanandroid.ui.collection.CollectionActivity
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity
import com.xiaojianjun.wanandroid.ui.detail.DetailActivity.Companion.PARAM_ARTICLE
import com.xiaojianjun.wanandroid.ui.history.HistoryActivity
import com.xiaojianjun.wanandroid.ui.opensource.OpenSourceActivity
import com.xiaojianjun.wanandroid.ui.points.mine.MinePointsActivity
import com.xiaojianjun.wanandroid.ui.points.rank.PointsRankActivity
import com.xiaojianjun.wanandroid.ui.settings.SettingsActivity
import com.xiaojianjun.wanandroid.ui.shared.SharedActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseVmFragment<ProfileViewModel>() {

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun layoutRes() = R.layout.fragment_profile

    override fun viewModelClass() = ProfileViewModel::class.java

    override fun initView() {
        clHeader.setOnClickListener {
            checkLogin { /*上传头像，暂不支持*/ }
        }
        llMyPoints.setOnClickListener {
            checkLogin { ActivityHelper.start(MinePointsActivity::class.java) }
        }
        llPointsRank.setOnClickListener {
            ActivityHelper.start(PointsRankActivity::class.java)
        }
        llMyShare.setOnClickListener {
            checkLogin { ActivityHelper.start(SharedActivity::class.java) }
        }
        llMyCollect.setOnClickListener {
            checkLogin { ActivityHelper.start(CollectionActivity::class.java) }
        }
        llHistory.setOnClickListener {
            ActivityHelper.start(HistoryActivity::class.java)
        }
        llAboutAuthor.setOnClickListener {
            ActivityHelper.start(
                DetailActivity::class.java,
                mapOf(
                    PARAM_ARTICLE to Article(
                        title = getString(R.string.my_about_author),
                        link = "https://github.com/jianjunxiao"
                    )
                )
            )
        }
        llOpenSource.setOnClickListener {
            ActivityHelper.start(OpenSourceActivity::class.java)
        }
        llSetting.setOnClickListener {
            ActivityHelper.start(SettingsActivity::class.java)
        }

        updateUi()
    }

    override fun observe() {
        super.observe()
        Bus.observe<Boolean>(USER_LOGIN_STATE_CHANGED, viewLifecycleOwner, {
            updateUi()
        })
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
