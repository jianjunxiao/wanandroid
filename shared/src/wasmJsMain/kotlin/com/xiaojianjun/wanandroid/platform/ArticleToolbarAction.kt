package com.xiaojianjun.wanandroid.platform

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.xiaojianjun.wanandroid.ui.compose.theme.WanTheme

@JsFun("(url) => { const target=new URL(url); if(target.protocol==='https:' || target.protocol==='http:') window.open(target.href, '_blank', 'noopener,noreferrer'); }")
private external fun openOriginalArticle(url: String)

private val OpenOriginalIcon = ImageVector.Builder("OpenOriginal", 24.dp, 24.dp, 24f, 24f).apply {
    path(fill = SolidColor(Color.Black)) {
        moveTo(19f, 19f); lineTo(5f, 19f); lineTo(5f, 5f); lineTo(12f, 5f); lineTo(12f, 3f)
        lineTo(5f, 3f); curveTo(3.9f, 3f, 3f, 3.9f, 3f, 5f); lineTo(3f, 19f)
        curveTo(3f, 20.1f, 3.9f, 21f, 5f, 21f); lineTo(19f, 21f)
        curveTo(20.1f, 21f, 21f, 20.1f, 21f, 19f); lineTo(21f, 12f); lineTo(19f, 12f); close()
        moveTo(14f, 3f); lineTo(14f, 5f); lineTo(17.59f, 5f); lineTo(7.76f, 14.83f)
        lineTo(9.17f, 16.24f); lineTo(19f, 6.41f); lineTo(19f, 10f); lineTo(21f, 10f)
        lineTo(21f, 3f); close()
    }
}.build()

@Composable
actual fun ArticleToolbarAction(url: String, modifier: Modifier) {
    IconButton(onClick = { openOriginalArticle(url) }, modifier = modifier) {
        Icon(OpenOriginalIcon, contentDescription = "在新标签页打开原文", tint = WanTheme.colors.textPrimary)
    }
}
