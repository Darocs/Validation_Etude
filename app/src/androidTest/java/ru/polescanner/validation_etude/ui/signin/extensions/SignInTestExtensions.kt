package ru.polescanner.validation_etude.ui.signin.extensions

import com.atiurin.ultron.core.compose.nodeinteraction.UltronComposeSemanticsNodeInteraction
import ru.polescanner.validation_etude.domain.general.DI.login
import ru.polescanner.validation_etude.ui.reusable.components.AssertStateDescription.assertStateDescriptionContains

data class Config(
    val validChar: Char = '1',
    val invalChar: Char = 'A'
)

val config: Config = Config()

object LoginExtensions {
    fun UCS.assertIsOkay2() : UCS = this
        .assertContentDescriptionContains("label")
        .assertTextContains(config.validChar.times(2))
        .assertContentDescriptionContains("supportingText")
        .assertStateDescriptionContains("valid")
}

object CommonExtensions {
    fun UCS.assertIsOkay0() : UCS = this
        .assertTextContains("")
        .assertContentDescriptionContains("label")
        .assertContentDescriptionContains("supportingText")
        .assertStateDescriptionContains("valid")

    fun UCS.assertIsMin(valid: Boolean) : UCS = this
        .assertContentDescriptionContains("label*")
        .assertTextContains("${if (valid) config.validChar else config.invalChar}")
        .assertContentDescriptionContains("supportingText*")
        .assertTextContains("Min ${login?.min} chars")
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

fun Char.times(n: Int = 1) : String = "$this".repeat(n)
