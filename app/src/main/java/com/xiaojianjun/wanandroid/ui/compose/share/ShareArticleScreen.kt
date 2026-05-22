package com.xiaojianjun.wanandroid.ui.compose.share

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xiaojianjun.wanandroid.R
import com.xiaojianjun.wanandroid.ext.showToast
import com.xiaojianjun.wanandroid.ui.compose.components.ToolbarBottomShadow
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanNavigator
import com.xiaojianjun.wanandroid.ui.compose.theme.WanTheme

@Composable
fun ShareArticleScreen(
    navigator: WanNavigator,
    modifier: Modifier = Modifier,
    viewModel: ShareArticleComposeViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    var title by rememberSaveable { mutableStateOf("") }
    var link by rememberSaveable { mutableStateOf("") }

    LaunchedEffect(viewModel, context) {
        viewModel.shareEvents.collect { event ->
            when (event) {
                is ShareArticleEvent.Toast -> context.showToast(event.resId)
            }
        }
    }

    ShareArticleContent(
        state = state,
        title = title,
        link = link,
        onTitleChange = { title = it.take(100) },
        onLinkChange = { link = it },
        onBackClick = { navigator.goBack() },
        onSubmitClick = {
            val trimmedTitle = title.trim()
            val trimmedLink = link.trim()
            when {
                trimmedTitle.isEmpty() -> context.showToast(R.string.title_toast)
                trimmedLink.isEmpty() -> context.showToast(R.string.link_toast)
                else -> {
                    keyboardController?.hide()
                    viewModel.shareArticle(trimmedTitle, trimmedLink)
                }
            }
        },
        modifier = modifier,
    )

    if (state.submitting) {
        AlertDialog(
            onDismissRequest = {},
            text = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    CircularProgressIndicator(
                        color = WanTheme.colors.textPrimary,
                        modifier = Modifier.size(32.dp),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.sharing_article),
                        color = WanTheme.colors.textPrimary,
                        fontSize = 14.sp,
                    )
                }
            },
            confirmButton = {},
        )
    }
}

@Composable
private fun ShareArticleContent(
    state: ShareArticleUiState,
    title: String,
    link: String,
    onTitleChange: (String) -> Unit,
    onLinkChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WanTheme.colors.backgroundPrimary),
    ) {
        ShareToolbar(
            onBackClick = onBackClick,
            onSubmitClick = onSubmitClick,
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .background(WanTheme.colors.backgroundPrimary),
        ) {
            ShareLabel(
                text = stringResource(R.string.title),
                modifier = Modifier.padding(start = 16.dp, top = 32.dp),
            )
            ShareInput(
                value = title,
                onValueChange = onTitleChange,
                hint = stringResource(R.string.title_hint),
                minHeight = 62.dp,
                maxLines = 3,
                modifier = Modifier.padding(16.dp),
            )
            ShareLabel(
                text = stringResource(R.string.link),
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
            )
            ShareInput(
                value = link,
                onValueChange = onLinkChange,
                hint = stringResource(R.string.link_hint),
                minHeight = 82.dp,
                maxLines = 3,
                imeAction = ImeAction.Done,
                onImeAction = onSubmitClick,
                modifier = Modifier.padding(16.dp),
            )
            ShareLabel(
                text = stringResource(R.string.share_people),
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
            )
            ShareInput(
                value = state.sharePeople,
                onValueChange = {},
                hint = "",
                enabled = false,
                minHeight = 42.dp,
                maxLines = 1,
                modifier = Modifier.padding(16.dp),
            )
            ShareLabel(
                text = stringResource(R.string.tips),
                modifier = Modifier.padding(start = 16.dp, top = 16.dp),
            )
            Text(
                text = stringResource(R.string.tips_content),
                color = WanTheme.colors.textPrimary,
                fontSize = 13.sp,
                lineHeight = 17.sp,
                modifier = Modifier.padding(16.dp),
            )
        }
    }
}

@Composable
private fun ShareToolbar(
    onBackClick: () -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .shadow(2.dp)
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
            text = stringResource(R.string.share_article),
            color = WanTheme.colors.textPrimary,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.align(Alignment.Center),
        )
        Text(
            text = stringResource(R.string.submit),
            color = WanTheme.colors.textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .height(48.dp)
                .clickable(onClick = onSubmitClick)
                .padding(start = 16.dp, top = 15.dp, end = 16.dp),
        )
        ToolbarBottomShadow(modifier = Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun ShareLabel(
    text: String,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        color = WanTheme.colors.textPrimary,
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        modifier = modifier,
    )
}

@Composable
private fun ShareInput(
    value: String,
    onValueChange: (String) -> Unit,
    hint: String,
    minHeight: androidx.compose.ui.unit.Dp,
    maxLines: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Default,
    onImeAction: () -> Unit = {},
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        textStyle = TextStyle(
            color = WanTheme.colors.textPrimary,
            fontSize = 14.sp,
            lineHeight = 18.sp,
        ),
        maxLines = maxLines,
        cursorBrush = SolidColor(WanTheme.colors.textPrimary),
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        keyboardActions = KeyboardActions(onDone = { onImeAction() }),
        modifier = modifier
            .fillMaxWidth()
            .height(minHeight)
            .background(WanTheme.colors.backgroundThird, RoundedCornerShape(2.dp))
            .padding(8.dp),
        decorationBox = { innerTextField ->
            Box(modifier = Modifier.fillMaxSize()) {
                if (value.isEmpty() && hint.isNotEmpty()) {
                    Text(
                        text = hint,
                        color = WanTheme.colors.textThird,
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        maxLines = maxLines,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                innerTextField()
            }
        },
    )
}
