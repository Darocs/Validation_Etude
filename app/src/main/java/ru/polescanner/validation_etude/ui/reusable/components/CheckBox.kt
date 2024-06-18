package ru.polescanner.validation_etude.ui.reusable.components

import android.annotation.SuppressLint
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.triStateToggleable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TriStateCheckbox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CheckBoxWithText(
    label: String?,
    uiState: Boolean?,
    isFocused: Boolean = false,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier,
    onClick: (Boolean) -> Unit
) {
    val focusRequester = remember { FocusRequester() }
    val toggleableUiState = uiState.toToggleableState()
    val focusManager = LocalFocusManager.current

    if (isFocused) focusRequester.requestFocus()

    Row(
        modifier = modifier
            .triStateToggleable(state = toggleableUiState,
                                interactionSource = remember { MutableInteractionSource() },
                                indication = LocalIndication.current,
                                role = Role.Checkbox,
                                onClick = {
                                    onClick(uiState.nextState())
                                    focusManager.clearFocus()
                                })
            .semantics { contentDescription = "checkBox" }
            .focusRequester(focusRequester)
            .focusable(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TriStateCheckbox(
            state = toggleableUiState,
            onClick = null,
            modifier = Modifier
                .width(25.dp)
                .height(25.dp),
        )
        label?.let {
            Text(
                text = AnnotatedString(it),
                modifier = Modifier.padding(end = 5.dp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = if (isFocused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground
                )
            )
        }
    }
}

private fun Boolean?.toToggleableState() =
    this?. let {
        if (it) ToggleableState.On
        else ToggleableState.Off
    } ?: ToggleableState.Indeterminate

private fun Boolean?.nextState() =
    this?.let{ !it } ?: true

@Preview(showBackground = true)
@Composable
fun CheckBoxWithTextPreviewTrue() {
    CheckBoxWithText(
        label = "Some text for checkBox",
        uiState = true
    ) {}
}

@Preview(showBackground = true)
@Composable
fun CheckBoxWithTextPreviewFalse() {
    CheckBoxWithText(
        label = "Some text for checkBox",
        uiState = false
    ) {}
}

fun onIndeterminateClick(value: Boolean?): Boolean = value.nextState()
