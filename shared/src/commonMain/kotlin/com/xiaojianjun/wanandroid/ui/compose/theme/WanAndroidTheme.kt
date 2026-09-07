package com.xiaojianjun.wanandroid.ui.compose.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class WanColors(
    val primary: Color,
    val accent: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textThird: Color,
    val backgroundPrimary: Color,
    val backgroundSecondary: Color,
    val backgroundThird: Color,
    val ripple: Color,
    val badge: Color,
)

private val LightWanColors = WanColors(
    primary = Color(0xFFFFFFFF),
    accent = Color(0xFF1B1B1D),
    textPrimary = Color(0xFF1B1B1D),
    textSecondary = Color(0xFF6B7178),
    textThird = Color(0xFF9297A2),
    backgroundPrimary = Color(0xFFFFFFFF),
    backgroundSecondary = Color(0xFFF8F8F8),
    backgroundThird = Color(0xFFE8E8E8),
    ripple = Color(0xFFE2E2E2),
    badge = Color(0xFFFC1603),
)

private val DarkWanColors = WanColors(
    primary = Color(0xFF303030),
    accent = Color(0xFFB1B1B1),
    textPrimary = Color(0xFFB1B1B1),
    textSecondary = Color(0xFF7C7B7B),
    textThird = Color(0xFF5E5E5E),
    backgroundPrimary = Color(0xFF303030),
    backgroundSecondary = Color(0xFF292929),
    backgroundThird = Color(0xFF262626),
    ripple = Color(0xFF3F3F3F),
    badge = Color(0xFFF44336),
)

val LocalWanColors = staticCompositionLocalOf { LightWanColors }

object WanTheme {
    val colors: WanColors
        @Composable get() = LocalWanColors.current
}

@Composable
fun WanAndroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val wanColors = if (darkTheme) DarkWanColors else LightWanColors
    val materialColors = if (darkTheme) {
        darkColorScheme(
            primary = wanColors.textPrimary,
            background = wanColors.backgroundPrimary,
            surface = wanColors.backgroundPrimary,
            onPrimary = wanColors.backgroundPrimary,
            onBackground = wanColors.textPrimary,
            onSurface = wanColors.textPrimary,
        )
    } else {
        lightColorScheme(
            primary = wanColors.textPrimary,
            background = wanColors.backgroundPrimary,
            surface = wanColors.backgroundPrimary,
            onPrimary = wanColors.backgroundPrimary,
            onBackground = wanColors.textPrimary,
            onSurface = wanColors.textPrimary,
        )
    }

    androidx.compose.runtime.CompositionLocalProvider(LocalWanColors provides wanColors) {
        MaterialTheme(
            colorScheme = materialColors,
            content = content,
        )
    }
}
