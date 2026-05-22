package com.xiaojianjun.wanandroid.ui.compose.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xiaojianjun.wanandroid.BuildConfig
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.ext.showToast
import com.xiaojianjun.wanandroid.ui.compose.components.ToolbarBottomShadow
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanNavigator
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute
import com.xiaojianjun.wanandroid.ui.compose.theme.WanTheme

@Composable
fun SettingsScreen(
    navigator: WanNavigator,
    modifier: Modifier = Modifier,
    onNightModeChanged: (Boolean) -> Unit = {},
    viewModel: SettingsComposeViewModel = viewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var showClearCacheDialog by rememberSaveable { mutableStateOf(false) }
    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }
    var showTextZoomDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(context, viewModel) {
        viewModel.refresh(context)
    }

    SettingsContent(
        state = state,
        onBackClick = { navigator.goBack() },
        onNightModeChange = {
            viewModel.setNightModeEnabled(it)
            onNightModeChanged(it)
        },
        onTextZoomClick = { showTextZoomDialog = true },
        onClearCacheClick = { showClearCacheDialog = true },
        onCheckVersionClick = { context.showToast(context.getString(R.string.stay_tuned)) },
        onAboutClick = {
            navigator.navigate(
                WanRoute.ArticleDetail(
                    id = 0L,
                    title = context.getString(R.string.abount_us),
                    link = "https://github.com/jianjunxiao/wanandroid",
                ),
            )
        },
        onLogoutClick = { showLogoutDialog = true },
        modifier = modifier,
    )

    if (showClearCacheDialog) {
        ConfirmDialog(
            message = stringResource(R.string.confirm_clear_cache),
            onConfirm = {
                viewModel.clearCacheAndRefresh(context)
                showClearCacheDialog = false
            },
            onDismiss = { showClearCacheDialog = false },
        )
    }

    if (showLogoutDialog) {
        ConfirmDialog(
            message = stringResource(R.string.confirm_logout),
            onConfirm = {
                viewModel.logout()
                showLogoutDialog = false
                navigator.replace(WanRoute.Login)
            },
            onDismiss = { showLogoutDialog = false },
        )
    }

    if (showTextZoomDialog) {
        TextZoomDialog(
            textZoom = state.webTextZoom,
            onConfirm = {
                viewModel.setWebTextZoom(it)
                showTextZoomDialog = false
            },
            onDismiss = { showTextZoomDialog = false },
        )
    }
}

@Composable
private fun SettingsContent(
    state: SettingsUiState,
    onBackClick: () -> Unit,
    onNightModeChange: (Boolean) -> Unit,
    onTextZoomClick: () -> Unit,
    onClearCacheClick: () -> Unit,
    onCheckVersionClick: () -> Unit,
    onAboutClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WanTheme.colors.backgroundSecondary),
    ) {
        SettingsToolbar(onBackClick = onBackClick)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState()),
        ) {
            SettingsSection(modifier = Modifier.padding(top = 8.dp)) {
                SettingsSwitchRow(
                    title = stringResource(R.string.night_mode),
                    checked = state.nightMode,
                    onCheckedChange = onNightModeChange,
                )
                SettingsDivider()
                SettingsRow(
                    title = stringResource(R.string.font_size),
                    value = "${state.webTextZoom}%",
                    onClick = onTextZoomClick,
                )
                SettingsDivider()
                SettingsRow(
                    title = stringResource(R.string.clear_cache),
                    value = state.cacheSize,
                    onClick = onClearCacheClick,
                )
            }
            SettingsSection(modifier = Modifier.padding(top = 8.dp)) {
                SettingsRow(
                    title = stringResource(R.string.check_version),
                    value = stringResource(R.string.already_latest_version),
                    onClick = onCheckVersionClick,
                )
                SettingsDivider()
                SettingsRow(
                    title = stringResource(R.string.abount_us),
                    value = stringResource(R.string.current_version, BuildConfig.VERSION_NAME),
                    onClick = onAboutClick,
                )
            }
            if (state.isLogin) {
                Text(
                    text = stringResource(R.string.logout),
                    color = WanTheme.colors.textPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 8.dp)
                        .shadow(1.dp)
                        .background(WanTheme.colors.backgroundPrimary)
                        .clickable(onClick = onLogoutClick)
                        .height(46.dp)
                        .wrapContentHeight(Alignment.CenterVertically),
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun SettingsToolbar(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(WanTheme.colors.backgroundPrimary),
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 4.dp, end = 4.dp)
                .size(48.dp),
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back_black_24dp),
                contentDescription = null,
                tint = WanTheme.colors.textPrimary,
                modifier = Modifier.size(24.dp),
            )
        }
        Text(
            text = stringResource(R.string.system_settings),
            color = WanTheme.colors.textPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Center),
        )
        ToolbarBottomShadow(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun SettingsSection(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp)
            .background(WanTheme.colors.backgroundPrimary),
    ) {
        content()
    }
}

@Composable
private fun SettingsRow(
    title: String,
    value: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .clickable(onClick = onClick)
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            color = WanTheme.colors.textPrimary,
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = value,
            color = WanTheme.colors.textPrimary,
            fontSize = 13.sp,
            modifier = Modifier.padding(end = 6.dp),
        )
    }
}

@Composable
private fun SettingsSwitchRow(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(46.dp)
            .padding(start = 16.dp, end = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = title,
            color = WanTheme.colors.textPrimary,
            fontSize = 14.sp,
        )
        Spacer(modifier = Modifier.weight(1f))
        LegacySettingsSwitch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
private fun LegacySettingsSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    val trackColor = if (checked) WanTheme.colors.accent else Color(0xFFBDBDBD)
    Box(
        modifier = modifier
            .size(width = 38.dp, height = 22.dp)
            .clip(RoundedCornerShape(11.dp))
            .background(trackColor.copy(alpha = if (checked) 0.55f else 0.45f))
            .toggleable(
                value = checked,
                role = Role.Switch,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onValueChange = onCheckedChange,
            )
            .padding(horizontal = 2.dp),
        contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .size(18.dp)
                .clip(CircleShape)
                .background(if (checked) WanTheme.colors.accent else Color.White),
        )
    }
}

@Composable
private fun SettingsDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(WanTheme.colors.backgroundSecondary),
    )
}

@Composable
private fun ConfirmDialog(
    message: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Text(
                text = message,
                color = WanTheme.colors.textPrimary,
                fontSize = 14.sp,
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        },
    )
}

@Composable
private fun TextZoomDialog(
    textZoom: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var currentTextZoom by remember(textZoom) { mutableIntStateOf(textZoom) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.font_size),
                color = WanTheme.colors.textPrimary,
                fontSize = 16.sp,
            )
        },
        text = {
            Slider(
                value = currentTextZoom.toFloat(),
                onValueChange = { currentTextZoom = it.toInt() },
                valueRange = 50f..150f,
                steps = 99,
                modifier = Modifier.padding(start = 12.dp, top = 24.dp, end = 12.dp, bottom = 16.dp),
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(currentTextZoom) }) {
                Text(text = stringResource(R.string.confirm))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        },
    )
}
