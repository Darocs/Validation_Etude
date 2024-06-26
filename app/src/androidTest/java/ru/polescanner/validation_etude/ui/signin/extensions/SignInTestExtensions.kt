package ru.polescanner.validation_etude.ui.signin.extensions

import com.atiurin.ultron.core.compose.nodeinteraction.UltronComposeSemanticsNodeInteraction
import ru.polescanner.validation_etude.domain.general.DI.login
import ru.polescanner.validation_etude.ui.reusable.components.AssertStateDescription.assertStateDescriptionContains
import ru.polescanner.validation_etude.ui.reusable.components.TextField.getEditableText

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

    fun UCS.assertIsInval2() : UCS {
        val text = this.getEditableText()!!
        require(config.invalChar in text) { "Text doesn't have inval char" }
        require(text.length == 2) { "Inval2 must contain only 2 characters" }
        return this
            .assertContentDescriptionContains("label*")
            .assertContentDescriptionContains("supportingText*")
            .assertTextContains("Allowed chars: ${login?.regex}")
            .assertStateDescriptionContains("invalid")
    }
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

    fun UCS.assertIsMax() : UCS {
        require(this.getEditableText()!!.length > 3)
        return this.assertContentDescriptionContains("label*")
            .assertContentDescriptionContains("supportingText*")
            .assertTextContains("Max ${login?.max} chars")
            .assertStateDescriptionContains("invalid")
    }

    fun UCS.assertIsInval3() : UCS {
        val text = this.getEditableText()!!
        require(config.invalChar in text) { "Text doesn't have inval char" }
        require(text.length == 3) { "Inval2 must contain only 3 characters" }
        return this
            .assertContentDescriptionContains("label*")
            .assertContentDescriptionContains("supportingText*")
            .assertTextContains("Allowed chars: ${login?.regex}")
            .assertStateDescriptionContains("invalid")
    }
}

typealias UCS = UltronComposeSemanticsNodeInteraction

fun Char.times(n: Int = 1) : String = "$this".repeat(n)
