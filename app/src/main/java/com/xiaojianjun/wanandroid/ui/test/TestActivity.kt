package com.xiaojianjun.wanandroid.ui.test

import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.lifecycleScope
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.base.BaseVmActivity
import com.xiaojianjun.wanandroid.ext.showToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class TestActivity : BaseVmActivity<TestViewModel>() {

    override fun layoutRes() = R.layout.activity_test

    override fun viewModelClass() = TestViewModel::class.java

    override fun initView() {
        startStep()
    }

    private fun startStep() {
        lifecycleScope.launch {
            load()
            if (!showStep1()) return@launch
            load()
            if (!showStep2()) return@launch
            load()
            if (!showStep3()) return@launch
            load()
            if (!showConfirmDialog()) return@launch
            showToast("正在退出...")
            delay(2000)
            finish()
        }
    }

    private suspend fun load() {
        showProgressDialog(R.string.loading)
        delay(2000)
        dismissProgressDialog()
    }

    private suspend fun showStep1(): Boolean {
        return suspendCancellableCoroutine {
            AlertDialog.Builder(this)
                .setMessage("确认第1步？")
                .setPositiveButton("确认") { _, _ -> it.resume(true) }
                .setNegativeButton("取消") { _, _ -> it.resume(false) }
                .show()
        }
    }

    private suspend fun showStep2(): Boolean {
        return suspendCancellableCoroutine {
            AlertDialog.Builder(this)
                .setMessage("确认第2步？")
                .setPositiveButton("确认") { _, _ -> it.resume(true) }
                .setNegativeButton("取消") { _, _ -> it.resume(false) }
                .show()
        }
    }


    private suspend fun showStep3(): Boolean {
        return suspendCancellableCoroutine {
            AlertDialog.Builder(this)
                .setMessage("确认第3步？")
                .setPositiveButton("确认") { _, _ -> it.resume(true) }
                .setNegativeButton("取消") { _, _ -> it.resume(false) }
                .show()
        }
    }

    private suspend fun showConfirmDialog(): Boolean {
        return suspendCancellableCoroutine {
            AlertDialog.Builder(this)
                .setMessage("完成3步确认，是否退出？")
                .setPositiveButton("退出") { _, _ -> it.resume(true) }
                .setNegativeButton("取消") { _, _ -> it.resume(false) }
                .show()
        }
    }
}