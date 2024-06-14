package ru.polescanner.validation_etude.ui.reusable.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import ru.polescanner.validation_etude.R
import ru.polescanner.validation_etude.domain.general.toLogin
import ru.polescanner.validation_etude.domain.general.toPassword
import ru.polescanner.validation_etude.ui.reusable.util.UiText
import ru.polescanner.validation_etude.ui.reusable.util.ValidOrFocusedAtCheck
import ru.polescanner.validation_etude.ui.reusable.util.toMessage
import ru.polescanner.validation_etude.ui.reusable.util.withClearedFocus

@Composable
fun RegistrationBlock(
    loginName: ValidOrFocusedAtCheck<String>,
    onValidLoginName: (String) -> Unit,
    password: ValidOrFocusedAtCheck<String>,
    onPasswordChanged: (String) -> Unit,
    start: Boolean,
) {
    Column {
        LoginElement(
            login = loginName.value,
            onValueChange = onValidLoginName,
            isFocused = loginName.isFocused
        )
        PasswordElement(
            password = password.value,
            onValid = onPasswordChanged, //ToDo - not match with OnValid for Login Element
            isFocused = password.isFocused
        )
    }
}

@Composable
fun LoginElement(
    login: String,
    onValueChange: (String) -> Unit,
    isFocused: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    login.toLogin().let {
        val isError = it.isLeft() && login.isNotBlank()
        val supportingText = if (isError) it.leftOrNull()!!.toMessage() else UiText.Res(R.string.login)
        ValidatedOutlinedTextField(
            text = login,
            onValueChange = onValueChange,
            isError = isError,
            label = UiText.Res(R.string.login),
            placeholder = UiText.Res(R.string.enter_login),
            supportingText = supportingText,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            isFocused = isFocused,
            modifier = modifier
        )
    }
}

@Composable
fun PasswordElement(
    password: String,
    onValid: (String) -> Unit,
    isFocused: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    password.toPassword().let {
        val isError = it.isLeft() && password.isNotBlank()
        val supportingText = if (isError) it.leftOrNull()!!.toMessage() else UiText.Res(R.string.password)
        ValidatedOutlinedTextField(
            text = password,
            onValueChange = onValid,
            isError = isError,
            label = UiText.Res(R.string.password),
            placeholder = UiText.Res(R.string.enter_password),
            supportingText = supportingText,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next),
            visualTransformation = PasswordVisualTransformation(),
            hideText = true,
            isFocused = isFocused,
            modifier = modifier
        )
    }
}

@Composable
@Preview(showSystemUi = true)
private fun RegistrationBlockPreview() {
    RegistrationBlock(
        loginName = "SabNK".withClearedFocus(),
        onValidLoginName = {},
        password = "123456".withClearedFocus(),
        onPasswordChanged = {},
        start = false
    )
}
