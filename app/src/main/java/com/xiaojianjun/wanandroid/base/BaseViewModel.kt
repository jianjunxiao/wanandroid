package com.xiaojianjun.wanandroid.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParseException
import com.xiaojianjun.wanandroid.App
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.ext.showToast
import com.xiaojianjun.wanandroid.model.api.ApiException
import com.xiaojianjun.wanandroid.model.api.RetrofitClient
import com.xiaojianjun.wanandroid.model.store.UserInfoStore
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

typealias Block<T> = suspend (CoroutineScope) -> T
typealias Error = suspend (Exception) -> Unit
typealias Cancel = suspend (Exception) -> Unit

/**
 * Created by xiaojianjun on 2019-09-20.
 */
open class BaseViewModel : ViewModel() {

    val loginStatusInvalid: MutableLiveData<Boolean> = MutableLiveData()

    /**
     * 创建并执行协程
     * @param block 协程中执行
     * @param error 错误时执行
     * @param cancel 取消时只需
     * @param showErrorToast 是否弹出错误吐司
     * @return Job
     */
    protected fun launch(
        block: Block<Unit>,
        error: Error? = null,
        cancel: Cancel? = null,
        showErrorToast: Boolean = true
    ): Job {
        return viewModelScope.launch {
            try {
                block.invoke(this)
            } catch (e: Exception) {
                when (e) {
                    is CancellationException -> {
                        cancel?.invoke(e)
                    }
                    else -> {
                        onError(e, showErrorToast)
                        error?.invoke(e)
                    }
                }
            }
        }
    }

    /**
     * 创建并执行协程
     * @param block 协程中执行
     * @return Deferred<T>
     */
    protected fun <T> async(block: Block<T>): Deferred<T> {
        return viewModelScope.async { block.invoke(this) }
    }

    /**
     * 取消协程
     * @param job 协程job
     */
    protected fun cancelJob(job: Job?) {
        if (job != null && job.isActive && !job.isCompleted && !job.isCancelled) {
            job.cancel()
        }
    }

    /**
     * 统一处理错误
     * @param e 异常
     * @param showErrorToast 是否显示错误吐司
     */
    private fun onError(e: Exception, showErrorToast: Boolean) {
        when (e) {
            is ApiException -> {
                when (e.code) {
                    -1001 -> {
                        // 登录失效，清除用户信息、清除cookie/token
                        UserInfoStore.clearUserInfo()
                        RetrofitClient.clearCookie()
                        loginStatusInvalid.value = true
                    }
                    // 其他api错误
                    -1 -> if (showErrorToast) App.instance.showToast(e.message)
                    // 其他错误
                    else -> if (showErrorToast) App.instance.showToast(e.message)
                }
            }
            // 网络请求失败
            is ConnectException, is SocketTimeoutException, is UnknownHostException, is HttpException ->
                if (showErrorToast) App.instance.showToast(R.string.network_request_failed)
            // 数据解析错误
            is JsonParseException ->
                if (showErrorToast) App.instance.showToast(R.string.api_data_parse_error)
            // 其他错误
            else ->
                if (showErrorToast) App.instance.showToast(e.message ?: return)
        }
    }
}