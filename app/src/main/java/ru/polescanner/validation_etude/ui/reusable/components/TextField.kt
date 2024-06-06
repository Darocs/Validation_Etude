package ru.polescanner.validation_etude.ui.reusable.components

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.OutlinedTextFieldDefaults.colors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import arrow.core.Either
import ru.polescanner.validation_etude.R
import ru.polescanner.validation_etude.domain.general.ErrorType
import ru.polescanner.validation_etude.domain.general.Login
import ru.polescanner.validation_etude.domain.general.DomainPrimitive
import ru.polescanner.validation_etude.ui.reusable.util.UiText
import ru.polescanner.validation_etude.ui.reusable.util.toMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <V: DomainPrimitive> ValidatedOutlinedTextField(
    text: String,
    onValid: (String) -> Unit,
    parse: (String) -> Either<ErrorType, V>,
    isError: Boolean = false,
    setError: (Boolean) -> Unit = {},
    @StringRes labelRes: Int = R.string.void_text,
    @StringRes placeholderRes: Int = R.string.void_text,
    @StringRes supportingTextRes: Int = R.string.void_text,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    hideText: Boolean = false,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    isFocused: Boolean = false,
    shape: Shape = OutlinedTextFieldDefaults.shape,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    var inputText by remember { mutableStateOf(text) }
    var errorMessageRes by remember { mutableStateOf<UiText>(UiText.Res(R.string.app_name)) }
    var passwordVisibility by remember { mutableStateOf(hideText) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val colors = colors().copy(
        focusedContainerColor = colorScheme.surfaceContainer,
        unfocusedContainerColor = colorScheme.onSecondary,
        unfocusedLabelColor = colorScheme.onSurface
    )

    if (isFocused) focusRequester.requestFocus()

    fun String.toDomainPrimitive() = parse(this)

    fun validate(unvalidated: String) = unvalidated.toDomainPrimitive()
        .fold({
            setError(true)
            errorMessageRes = it.toMessage()
        }, {
            setError(false)
            onValid(unvalidated)
        })

    BasicTextField(
        value = inputText,
        onValueChange = {
            inputText = it
            validate(inputText)
        },
        modifier = modifier
            .focusRequester(focusRequester)
            .height(68.dp)
            .width(OutlinedTextFieldDefaults.MinWidth)
            .clickable {
                focusManager.clearFocus()
                focusRequester.requestFocus()
            },
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = if (!passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        interactionSource = interactionSource,
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() },
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ) { validate(inputText) },
        keyboardOptions = keyboardOptions,
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = colorScheme.onBackground),
        cursorBrush = SolidColor(colorScheme.secondary)
    ) { innerTextField ->
        OutlinedTextFieldDefaults.DecorationBox(
            value = inputText,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            label = {
                Text(
                    if (isError) stringResource(id = labelRes) + "*"
                    else stringResource(id = labelRes),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                )
            },
            placeholder = {
                Text(
                    text = stringResource(id = placeholderRes),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp)
                )
            },
            isError = isError,
            trailingIcon = {
                if (visualTransformation == PasswordVisualTransformation()) {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            imageVector = if (!passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = "visibility"
                        )
                    }
                } else if (isError) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "error",
                        tint = colorScheme.error
                    )
                } else {
                    IconButton(onClick = { inputText = "" }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "clear text"
                        )
                    }
                }
            },
            supportingText = {
                if (isError) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = errorMessageRes.asString(),
                        color = colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1
                    )
                } else {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = UiText.Res(supportingTextRes).asString(),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            },
            contentPadding = OutlinedTextFieldDefaults.contentPadding(
                top = 0.dp,
                bottom = 0.dp
            ),
            colors = colors,
            container = {
                OutlinedTextFieldDefaults.ContainerBox(
                    enabled = true,
                    isError = isError,
                    interactionSource = interactionSource,
                    colors = colors,
                    shape = shape
                )
            }
        )
    }
}

@Preview(
    showBackground = true,
    name = "Valid"
)
@Composable
fun ValidatedOutlinedTextFieldPreview() {
    var isError by remember { mutableStateOf(false) }
    ValidatedOutlinedTextField(
        text = "Some text",
        onValid = {},
        parse = { Login(it) },
        isError = isError,
        setError = { isError = it },
        labelRes = R.string.alias_hint,
        placeholderRes = R.string.alias_hint,
        supportingTextRes = R.string.alias_helper_text
    )
}

@Preview(
    showBackground = true,
    name = "Invalid"
)
@Composable
fun InvalidWithoutTextPreview() {
    var isError by remember { mutableStateOf(false) }
    ValidatedOutlinedTextField(
        text = "",
        onValid = {},
        parse = { Login(it) },
        isError = isError,
        setError = { isError = it },
        labelRes = R.string.alias_hint,
        placeholderRes = R.string.alias_hint,
        supportingTextRes = R.string.alias_helper_text
    )
}

@Preview(
    showBackground = true,
    name = "Invalid"
)
@Composable
fun InvalidShortTextPreviewWith() {
    var isError by remember { mutableStateOf(true) }
    ValidatedOutlinedTextField(
        text = "asdsadsadsadsadsaaaaaaaa",
        onValid = {},
        parse = { Login(it) },
        isError = isError,
        setError = { isError = it },
        labelRes = R.string.alias_hint,
        placeholderRes = R.string.alias_hint,
        supportingTextRes = R.string.alias_helper_text
    )
}
