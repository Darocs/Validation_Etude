package ru.polescanner.validation_etude.ui.reusable.components

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import ru.polescanner.droidmvp.ui.reusable.util.ValidOrFocusedAtCheck
import ru.polescanner.droidmvp.ui.reusable.util.toFocusable
import ru.polescanner.droidmvp.ui.reusable.util.validateLogin
import ru.polescanner.droidmvp.ui.reusable.util.validateName
import ru.polescanner.droidmvp.ui.reusable.util.validatePassword
import ru.polescanner.validation_etude.R
import ru.polescanner.validation_etude.domainext.bus.Result
import ru.polescanner.validation_etude.ui.reusable.util.Notice
import ru.polescanner.validation_etude.ui.reusable.util.ValidOrFocusedAtCheck
import ru.polescanner.validation_etude.ui.reusable.util.toFocusable

@Composable
fun RegistrationBlock(
    loginName: ValidOrFocusedAtCheck<String>,
    onValidLoginName: (String) -> Unit,
    password: ValidOrFocusedAtCheck<String>,
    onPasswordChanged: (String) -> Unit,
    confirm: ValidOrFocusedAtCheck<String>,
    confirmValidator: (String) -> Result<Nothing, Notice<Int>>,
    onConfirmChanged: (String) -> Unit,
) {
    Column {
        LoginElement(
            login = loginName.value,
            onValid = onValidLoginName,
            isFocused = loginName.isFocused
        )
        PasswordElement(
            password = password.value,
            onValid = onPasswordChanged, //ToDo - not match with OnValid for Login Element
            isFocused = password.isFocused
        )
        ConfirmElement(
            confirm = confirm.value,
            validator = confirmValidator,
            onValid = onConfirmChanged
        )
    }
}

@Composable
fun LoginElement(
    login: String,
    onValid: (String) -> Unit,
    isFocused: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    var isError by remember { mutableStateOf(false) }

    ValidatedOutlinedTextField(
        text = login,
        onValid = onValid,
        parse = { it.validateLogin() },
        isError = isError,
        setError = { isError = it },
        labelRes = R.string.login,
        placeholderRes = R.string.enter_login,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        isFocused = isFocused,
        modifier = modifier
    )
}

@Composable
fun PasswordElement(
    password: String,
    onValid: (String) -> Unit,
    isFocused: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    var isError by remember { mutableStateOf(false) }
    ValidatedOutlinedTextField(
        text = password,
        onValid = onValid,
        parse = { it.validatePassword() },
        isError = isError,
        setError = { isError = it },
        labelRes = R.string.password,
        placeholderRes = R.string.enter_password,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
                                          imeAction = ImeAction.Next),
        visualTransformation = PasswordVisualTransformation(),
        hideText = true,
        isFocused = isFocused,
        modifier = modifier
    )
}

@Composable
fun ConfirmElement(
    confirm: String,
    validator: (String) -> Result<Nothing, Notice<Int>>,
    onValid: (String) -> Unit,
    isFocused: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    var isError by remember { mutableStateOf(false) }
    ValidatedOutlinedTextField(
        text = confirm,
        onValid = onValid,
        parse = validator,
        isError = isError,
        setError = { isError = it },
        labelRes = R.string.confirm_password,
        placeholderRes = R.string.confirm_password,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password,
                                          imeAction = ImeAction.Done),
        visualTransformation = PasswordVisualTransformation(),
        hideText = true,
        isFocused = isFocused,
        modifier = modifier
    )
}

@Composable
@Preview(showSystemUi = true)
private fun RegistrationBlockPreview() {
    RegistrationBlock(
        loginName = "SabNK".toFocusable(),
        onValidLoginName = {},
        password = "123456".toFocusable(),
        onPasswordChanged = {},
        confirm = "".toFocusable(),
        confirmValidator = { it.validateName() },
        onConfirmChanged = {},
    )
}
