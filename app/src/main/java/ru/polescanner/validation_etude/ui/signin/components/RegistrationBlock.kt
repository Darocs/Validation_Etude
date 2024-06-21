package ru.polescanner.validation_etude.ui.signin.components

import android.annotation.SuppressLint
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import ru.polescanner.validation_etude.R
import ru.polescanner.validation_etude.domain.general.toLogin
import ru.polescanner.validation_etude.domain.general.toPassword
import ru.polescanner.validation_etude.ui.reusable.components.CustomOutlinedTextField
import ru.polescanner.validation_etude.ui.reusable.util.UiText
import ru.polescanner.validation_etude.ui.reusable.util.toMessage

@Composable
fun LoginElement(
    login: String,
    onValueChange: (String) -> Unit,
    isFocused: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    login.toLogin().let {
        val isError = it.isLeft() && login.isNotBlank()
        val supportingText = if (isError) it.leftOrNull()!!.toMessage() else UiText.Res(R.string.login_example)
        CustomOutlinedTextField(
            text = login,
            onValueChange = onValueChange,
            isError = isError,
            label = UiText.Res(R.string.login),
            placeholder = UiText.Res(R.string.enter_login),
            supportingText = supportingText,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            isFocused = isFocused,
            modifier = modifier.semantics { contentDescription = "loginTag" }
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
        val supportingText = if (isError) it.leftOrNull()!!.toMessage() else UiText.Res(R.string.void_text)
        CustomOutlinedTextField(
            text = password,
            onValueChange = onValid,
            isError = isError,
            label = UiText.Res(R.string.password),
            placeholder = UiText.Res(R.string.enter_password),
            supportingText = supportingText,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            visualTransformation = PasswordVisualTransformation(),
            hideText = true,
            isFocused = isFocused,
            modifier = modifier.semantics { contentDescription = "password" }
        )
    }
}
