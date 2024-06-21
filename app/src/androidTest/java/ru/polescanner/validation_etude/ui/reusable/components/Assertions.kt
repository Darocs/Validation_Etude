@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package ru.polescanner.validation_etude.ui.reusable.components

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.hasStateDescription
import com.atiurin.ultron.core.common.UltronOperationType
import ru.polescanner.validation_etude.ui.signin.extensions.UCS

object AssertCheckBox {
    fun SemanticsMatcher.assertIsIndeterminate() = UCS(this).assertIsIndeterminate()

    fun UCS.assertIsIndeterminate() = apply {
        executeOperation(
            operationBlock = { semanticsNodeInteraction.assertIsIndeterminate() },
            name = "Assert '${elementInfo.name}' is indeterminate",
            type = MyOperationType.IS_INDETERMINATE,
            description = "Compose assertIsIndeterminate '${elementInfo.name}' during $timeoutMs ms",
        )
    }

    private fun SemanticsNodeInteraction.assertIsIndeterminate(): SemanticsNodeInteraction =
        assert(isIndeterminate())

    private fun isIndeterminate(): SemanticsMatcher = SemanticsMatcher.expectValue(
        SemanticsProperties.ToggleableState, ToggleableState.Indeterminate
    )
}

object AssertStateDescription {
    fun SemanticsMatcher.assertStateDescriptionContains(expected: String) = UCS(this).assertStateDescriptionContains(expected)

    fun UCS.assertStateDescriptionContains(expected: String) = apply {
        executeOperation(
            operationBlock = { semanticsNodeInteraction.assertStateDescriptionContains(expected) },
            name = "Assert StateDescription contains '$expected' in '${elementInfo.name}'",
            type = MyOperationType.STATE_DESCRIPTION_CONTAINS_TEXT,
            description = "Compose assertStateDescriptionContains '$expected' in '${elementInfo.name}' during $timeoutMs ms",
        )
    }

    private fun SemanticsNodeInteraction.assertStateDescriptionContains(value: String): SemanticsNodeInteraction =
        assert(hasStateDescription(value = value))
}

enum class MyOperationType : UltronOperationType {
    IS_INDETERMINATE, STATE_DESCRIPTION_CONTAINS_TEXT
} // Instead of ComposeOperationType in Ultron.