package ru.polescanner.validation_etude.ui.reusable.components

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.assert
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

    enum class MyOperationType : UltronOperationType {
        IS_INDETERMINATE
    } // Instead of ComposeOperationType in Ultron.

    private fun SemanticsNodeInteraction.assertIsIndeterminate(): SemanticsNodeInteraction =
        assert(isIndeterminate())

    private fun isIndeterminate(): SemanticsMatcher = SemanticsMatcher.expectValue(
        SemanticsProperties.ToggleableState, ToggleableState.Indeterminate
    )
}
