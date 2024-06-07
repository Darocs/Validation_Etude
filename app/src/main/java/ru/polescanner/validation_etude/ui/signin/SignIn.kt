package ru.polescanner.validation_etude.ui.signin


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.polescanner.core.general.User
import ru.polescanner.droidmvp.ui.reusable.components.LoadingScreen
import ru.polescanner.droidmvp.ui.reusable.util.toFocusable
import ru.polescanner.validation_etude.R
import ru.polescanner.validation_etude.ui.reusable.components.CheckBoxWithText
import ru.polescanner.validation_etude.ui.reusable.components.LoginElement
import ru.polescanner.validation_etude.ui.reusable.components.PasswordElement
import ru.polescanner.validation_etude.ui.reusable.components.onIndeterminateClick
import ru.polescanner.validation_etude.ui.reusable.util.UiText
import ru.polescanner.validation_etude.ui.reusable.util.ValidOrFocusedAtCheck

@Composable
fun SignInRoute(userId: String?,
                isLoggedIn: Boolean,
                onSuccess: (User) -> Unit, //Success is passed here
                modifier: Modifier = Modifier) {
    val viewmodel: SignInViewModel = viewModel(factory = SignInViewModel.Factory(
        navigator = navigator,
        userId = userId,
        isLoggedIn = isLoggedIn,
        onSuccess = onSuccess
    )
    )
    val uiState by viewmodel.stateFlow.collectAsStateWithLifecycle()

    when (uiState) {
        SignInState.Loading -> LoadingScreen(UiText.Str("SignIn Screen"))
        is SignInState.Main ->
            SignInScreen(
                uiState = uiState as SignInState.Main,
                onEvent = viewmodel::onEvent,
                modifier = modifier
            )
        is SignInState.Error -> viewmodel.error()
    }
}


@Composable
fun SignInScreen(
    uiState: SignInState.Main,
    onEvent: (SignInEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    SignInScreen(
        login = uiState.login,
        onLoginChanged = { onEvent(SignInEvent.OnLoginChanged(it)) },
        password = uiState.password,
        onPasswordChanged = { onEvent(SignInEvent.OnPasswordChanged(it)) },
        rememberMe = uiState.isLoggedIn,
        keepMeLoggedInChanged = {
            onEvent(
                SignInEvent.OnRememberMeFor30DaysChanged(
                    remember = !uiState.isLoggedIn
                )
            )
        },
        onLogin = {
            focusManager.clearFocus()
            onEvent(
                SignInEvent.OnLogin(
                    uiState.login.value,
                    uiState.password.value,
                    uiState.isLoggedIn
                )
            )
        },
        onSignUp = { onEvent(SignInEvent.OnSignUp) },
        onForgotPassword = { onEvent(SignInEvent.OnForgotPassword) },
        onLicense = { onEvent(SignInEvent.OnLicense) },
        modifier = modifier,
    )
}

@Composable
fun SignInScreen(
    login: ValidOrFocusedAtCheck<String>,
    onLoginChanged: (String) -> Unit,
    password: ValidOrFocusedAtCheck<String>,
    onPasswordChanged: (String) -> Unit,
    rememberMe: Boolean,
    keepMeLoggedInChanged: (Boolean) -> Unit,
    onLogin: () -> Unit,
    onSignUp: () -> Unit,
    onForgotPassword: () -> Unit,
    onLicense: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 120.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginElement(
            login = login.value,
            onValid = { onLoginChanged(it) },
            isFocused = login.isFocused
        )
        Spacer(Modifier.height(10.dp))
        PasswordElement(
            password = password.value,
            onValid = { onPasswordChanged(it) },
            isFocused = password.isFocused
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.toggleable(
                value = rememberMe,
                onValueChange = keepMeLoggedInChanged,
                role = Role.Checkbox
            )
        ) {
            CheckBoxWithText(
                label = UiText.Res(R.string.remember_me).asString(),
                uiState = rememberMe
            ) {
                keepMeLoggedInChanged(onIndeterminateClick(rememberMe))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onLogin) {
                Text(UiText.Res(R.string.submit).asString())
            }
            Button(onClick = onSignUp) {
                Text(UiText.Res(R.string.sign_up).asString())
            }
        }
        Text(
            text = UiText.Res(R.string.forgot_password).asString(),
            fontSize = 16.sp,
            modifier = Modifier
                .padding(
                    vertical = 6.dp
                )
                .clickable(onClick = onForgotPassword)
        )
        Text(text = UiText.Res(R.string.license).asString(),
             fontSize = 16.sp,
             modifier = Modifier
                 .clickable { onLicense() }
                 .padding(
                     vertical = 6.dp
                 )
        )
    }
}
@Preview(showSystemUi = true)
@Composable
fun SignInPreview2() {
    SignInScreen(
        uiState =  SignInState.Main(),
        onEvent =  {}
    )
}

@Preview(showSystemUi = true)
@Composable
fun SignInPreview() {
    SignInScreen(
        login = "".toFocusable(),
        onLoginChanged = {},
        password = "".toFocusable(),
        onPasswordChanged = {},
        rememberMe = false,
        keepMeLoggedInChanged = {},
        onLogin = {},
        onSignUp = {},
        onForgotPassword = {},
        onLicense = {},
        modifier = Modifier
    )
}
