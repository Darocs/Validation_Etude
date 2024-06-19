package ru.polescanner.validation_etude.ui.reusable.components

import android.annotation.SuppressLint
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
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.polescanner.validation_etude.R
import ru.polescanner.validation_etude.domain.general.Login
import ru.polescanner.validation_etude.ui.reusable.util.UiText

/**
 * Required LocalFocusManager & LocalSoftwareKeyboardController
 *
 */


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlinedTextField(
    text: String,
    onValueChange: (String) -> Unit,
    isError: Boolean = false,
    label: UiText = UiText.Str(""),
    placeholder: UiText = UiText.Str(""),
    supportingText: UiText = UiText.Str(""),
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

    BasicTextField(
        value = text,
        onValueChange = onValueChange,
        modifier = modifier
            .focusRequester(focusRequester)
            .height(68.dp)
            .width(OutlinedTextFieldDefaults.MinWidth)
            .clickable {
                focusManager.clearFocus()
                focusRequester.requestFocus()
            }
            .semantics(mergeDescendants = true) { stateDescription = if (isError) "invalid" else "valid" },
        singleLine = singleLine,
        maxLines = maxLines,
        visualTransformation = if (!passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
        interactionSource = interactionSource,
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() },
            onNext = { focusManager.moveFocus(FocusDirection.Down) }
        ),
        keyboardOptions = keyboardOptions,
        textStyle = MaterialTheme.typography.bodyMedium.copy(color = colorScheme.onBackground),
        cursorBrush = SolidColor(colorScheme.secondary)
    ) { innerTextField ->
        OutlinedTextFieldDefaults.DecorationBox(
            value = text,
            innerTextField = innerTextField,
            enabled = true,
            singleLine = singleLine,
            visualTransformation = visualTransformation,
            interactionSource = interactionSource,
            label = {
                Text(
                    text = if (isError) label.asString() + "*" else label.asString(),
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold, fontSize = 13.sp),
                    modifier = Modifier.semantics { contentDescription = if (isError) "invalid" else "valid" }
                )
            },
            placeholder = {
                Text(
                    text = placeholder.asString(),
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp)
                )
            },
            isError = isError,
            trailingIcon = {
                if (visualTransformation == PasswordVisualTransformation()) {
                    IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                        Icon(
                            imageVector = if (!passwordVisibility) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            /* ToDo to UiText.Res!!!
                             *  @param contentDescription text used by accessibility services to describe what this icon
                             *  represents. This should always be provided unless this icon is used for decorative purposes, and
                             *  does not represent a meaningful action that a user can take. This text should be localized, such
                             *  as by using [androidx.compose.ui.res.stringResource] or similar
                             */
                            contentDescription = "visibility"
                        )
                    }
                } else if (isError) {
                    Icon(
                        imageVector = Icons.Filled.Error,
                        contentDescription = "error",
                        tint = colorScheme.error,
                    )
                } else {
                    IconButton(onClick = { onValueChange("") }) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = "clear text"
                        )
                    }
                }
            },
            supportingText = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = supportingText.asString(),
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isError) colorScheme.error else colorScheme.onBackground
                )
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
    CustomOutlinedTextField(
        text = "Some text",
        onValueChange = {},
        isError = false,
        label = UiText.Res(R.string.alias_hint),
        placeholder = UiText.Res(R.string.alias_hint),
        supportingText = UiText.Res(R.string.alias_helper_text)
    )
}

@Preview(
    showBackground = true,
    name = "Invalid"
)
@Composable
fun InvalidWithoutTextPreview() {
    CustomOutlinedTextField(
        text = "",
        onValueChange = {},
        isError = false,
        label = UiText.Res(R.string.alias_hint),
        placeholder = UiText.Res(R.string.alias_hint),
        supportingText = UiText.Res(R.string.alias_helper_text)
    )
}

@Preview(
    showBackground = true,
    name = "Invalid"
)
@Composable
fun InvalidShortTextPreviewWith() {
    val text = "asdsadsadsadsadsaaaaaaaa"
    CustomOutlinedTextField(
        text = text,
        onValueChange = {},
        isError = Login(text).isLeft(),
        label = UiText.Res(R.string.alias_hint),
        placeholder = UiText.Res(R.string.alias_hint),
        supportingText = UiText.Res(R.string.alias_helper_text)
    )
}
