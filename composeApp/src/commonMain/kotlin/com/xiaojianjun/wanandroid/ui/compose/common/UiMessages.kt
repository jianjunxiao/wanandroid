package com.xiaojianjun.wanandroid.ui.compose.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

data class UiNotice(val text: String? = null, val resource: StringResource? = null, val version: Int = 0)

object UiMessages {
    private val notice = MutableStateFlow(UiNotice())
    val notices = notice.asStateFlow()
    private val login = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val loginRequests = login.asSharedFlow()
    fun requireLogin() { login.tryEmit(Unit) }
    fun showToast(message: String) = notice.update { UiNotice(text = message, version = it.version + 1) }
    fun showToast(resource: StringResource) = notice.update { UiNotice(resource = resource, version = it.version + 1) }
}

@Composable
fun UiMessageHost(modifier: Modifier = Modifier) {
    val notice by UiMessages.notices.collectAsState()
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(notice.version) {
        visible = notice.version > 0
        delay(2500)
        visible = false
    }
    if (visible) {
        val text = notice.resource?.let { stringResource(it) } ?: notice.text.orEmpty()
        Box(modifier.padding(24.dp).background(Color(0xDD323232), RoundedCornerShape(24.dp)).padding(16.dp, 10.dp)) {
            Text(text, color = Color.White, fontSize = 14.sp)
        }
    }
}
