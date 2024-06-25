package ru.polescanner.validation_etude.ui.signin.extensions

import com.atiurin.ultron.core.compose.nodeinteraction.UltronComposeSemanticsNodeInteraction
import ru.polescanner.validation_etude.domain.general.DI.login
import ru.polescanner.validation_etude.ui.reusable.components.AssertStateDescription.assertStateDescriptionContains

object LoginExtensions {
    fun UCS.assertIsOkay2(text: String) : UCS = this
        .assertTextContains(text)
        .assertContentDescriptionContains("label")
        .assertContentDescriptionContains("supportingText")
        .assertStateDescriptionContains("valid")
}

object CommonExtensions {
    fun UCS.assertIsOkay0() : UCS = this
        .assertTextContains("")
        .assertContentDescriptionContains("label")
        .assertContentDescriptionContains("supportingText")
        .assertStateDescriptionContains("valid")

    fun UCS.assertIsMin(text: String) : UCS = this
        .assertContentDescriptionContains("label*")
        .assertTextContains(text)
        .assertContentDescriptionContains("supportingText*")
        .assertContentDescriptionContains("Min ${login?.min} chars")
        .assertStateDescriptionContains("invalid")

    fun UCS.assertIsMax(text: String) : UCS = this
        .assertContentDescriptionContains("label*")
        .assertTextContains(text)
        .assertContentDescriptionContains("supportingText*")
        .assertContentDescriptionContains("Max ${login?.max} chars")
        .assertStateDescriptionContains("invalid")

    fun UCS.assertIsInvalidRegex(text: String) : UCS = this
        .assertContentDescriptionContains("label*")
        .assertTextContains(text)
        .assertContentDescriptionContains("supportingText*")
        .assertTextContains("Allowed chars: ${login?.regex}")
        .assertStateDescriptionContains("invalid")
}

typealias UCS = UltronComposeSemanticsNodeInteraction
