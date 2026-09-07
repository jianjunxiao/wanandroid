package com.xiaojianjun.wanandroid.ui.compose.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xiaojianjun.wanandroid.di.appViewModel
import com.xiaojianjun.wanandroid.resources.*
import com.xiaojianjun.wanandroid.ui.compose.navigation.WanNavigator
import com.xiaojianjun.wanandroid.ui.compose.theme.WanTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun LoginScreen(
    navigator: WanNavigator,
    modifier: Modifier = Modifier,
    viewModel: LoginComposeViewModel = appViewModel { LoginComposeViewModel(loginRepository) },
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel, navigator) {
        viewModel.loginEvents.collect { event ->
            when (event) {
                LoginEvent.LoginSuccess -> navigator.goBack()
            }
        }
    }

    LoginContent(
        state = state,
        onBack = { navigator.goBack() },
        onAccountChanged = viewModel::onAccountChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onSubmit = viewModel::submit,
        onRegister = { navigator.navigate(com.xiaojianjun.wanandroid.ui.compose.navigation.WanRoute.Register) },
        modifier = modifier,
    )
}

@Composable
fun LoginContent(
    state: LoginUiState,
    onBack: () -> Unit,
    onAccountChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    onRegister: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WanTheme.colors.backgroundPrimary),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(WanTheme.colors.backgroundPrimary),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(48.dp)
                    .clickable(onClick = onBack),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_close_black_24dp),
                    contentDescription = null,
                    tint = WanTheme.colors.textPrimary,
                    modifier = Modifier.size(24.dp),
                )
            }
            Text(
                text = stringResource(Res.string.login),
                color = WanTheme.colors.textPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.size(52.dp))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_android),
                contentDescription = null,
                tint = WanTheme.colors.textPrimary,
                modifier = Modifier
                    .padding(top = 48.dp)
                    .size(72.dp),
            )
            Text(
                text = stringResource(Res.string.wanandroid),
                color = WanTheme.colors.textPrimary,
                fontFamily = FontFamily.Serif,
                fontSize = 20.sp,
                modifier = Modifier.padding(top = 8.dp),
            )
            OutlinedTextField(
                value = state.account,
                onValueChange = onAccountChanged,
                label = { Text(stringResource(Res.string.account)) },
                isError = state.accountError != null,
                supportingText = state.accountError?.let { { Text(it) } },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 48.dp, end = 16.dp),
            )
            OutlinedTextField(
                value = state.password,
                onValueChange = onPasswordChanged,
                label = { Text(stringResource(Res.string.password)) },
                isError = state.passwordError != null,
                supportingText = state.passwordError?.let { { Text(it) } },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp, end = 16.dp),
            )
            Button(
                onClick = onSubmit,
                enabled = !state.isSubmitting,
                colors = ButtonDefaults.buttonColors(
                    containerColor = WanTheme.colors.backgroundThird,
                    contentColor = WanTheme.colors.textPrimary,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 48.dp, end = 16.dp)
                    .height(38.dp),
            ) {
                if (state.isSubmitting) {
                    CircularProgressIndicator(
                        color = WanTheme.colors.textPrimary,
                        modifier = Modifier.size(18.dp),
                    )
                } else {
                    Text(
                        text = stringResource(Res.string.login),
                        fontSize = 14.sp,
                    )
                }
            }
            Text(
                text = stringResource(Res.string.go_register),
                color = WanTheme.colors.textPrimary,
                fontSize = 14.sp,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 16.dp, end = 8.dp)
                    .clickable(onClick = onRegister)
                    .padding(8.dp),
            )
        }
    }
}
