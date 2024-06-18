package ru.polescanner.validation_etude.ui.signin


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.polescanner.validation_etude.LocalSnackbarHostState
import ru.polescanner.validation_etude.R
import ru.polescanner.validation_etude.ui.reusable.components.CheckBoxWithText
import ru.polescanner.validation_etude.ui.reusable.components.LoadingScreen
import ru.polescanner.validation_etude.ui.reusable.components.onIndeterminateClick
import ru.polescanner.validation_etude.ui.reusable.util.Focusable
import ru.polescanner.validation_etude.ui.reusable.util.UiText
import ru.polescanner.validation_etude.ui.reusable.util.withClearedFocus
import ru.polescanner.validation_etude.ui.reusable.util.withClearedFocusForNullable
import ru.polescanner.validation_etude.ui.signin.components.LoginElement
import ru.polescanner.validation_etude.ui.signin.components.PasswordElement

@Composable
fun SignInRoute(modifier: Modifier = Modifier) {

    val viewmodel: SignInViewModel = viewModel()
    val uiState by viewmodel.stateFlow.collectAsStateWithLifecycle()

    val snackbarHostState = LocalSnackbarHostState.current
    val snackbarNotice by viewmodel.snackbarText.collectAsStateWithLifecycle()
    val snackbarText = snackbarNotice.asString()

    LaunchedEffect(snackbarNotice, snackbarText) {
        if (snackbarText.isNotBlank()) snackbarHostState.showSnackbar(snackbarText)
    }

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
        rememberMe = uiState.rememberMe,
        onRememberMeChanged = { onEvent(SignInEvent.OnRememberMeChanged(it)) },
        onSubmitClick = {
            focusManager.clearFocus()
            onEvent(SignInEvent.OnSubmit)
        },
        modifier = modifier,
    )
}

@Composable
fun SignInScreen(
    login: Focusable<String>,
    onLoginChanged: (String) -> Unit,
    password: Focusable<String>,
    onPasswordChanged: (String) -> Unit,
    rememberMe: Focusable<Boolean?>,
    onRememberMeChanged: (Boolean) -> Unit,
    onSubmitClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.padding(vertical = 120.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginElement(
            login = login.value,
            onValueChange = { onLoginChanged(it) },
            isFocused = login.isFocused,
        )
        Spacer(Modifier.height(10.dp))
        PasswordElement(
            password = password.value,
            onValid = { onPasswordChanged(it) },
            isFocused = password.isFocused
        )
        Row( verticalAlignment = Alignment.CenterVertically,
        ) {
            CheckBoxWithText(
                label = UiText.Res(R.string.remember_me).asString(),
                uiState = rememberMe.value,
                isFocused = rememberMe.isFocused,
            ) {
                onRememberMeChanged(onIndeterminateClick(rememberMe.value))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = onSubmitClick,
                modifier = Modifier.semantics { contentDescription = "submit" }) {
                Text(UiText.Res(R.string.submit).asString())
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SignInPreview() {
    SignInScreen(
        login = "".withClearedFocus(),
        onLoginChanged = {},
        password = "".withClearedFocus(),
        onPasswordChanged = {},
        rememberMe = false.withClearedFocusForNullable(),
        onRememberMeChanged = {},
        onSubmitClick = {},
        modifier = Modifier
    )
}
