package ru.polescanner.validation_etude.ui.reusable.components

import androidx.compose.ui.semantics.getOrNull
import com.atiurin.ultron.core.compose.nodeinteraction.clickCenterRight
import ru.polescanner.validation_etude.ui.signin.extensions.Config
import ru.polescanner.validation_etude.ui.signin.extensions.UCS
import ru.polescanner.validation_etude.ui.signin.extensions.times

object TextField {
    val config: Config = Config()

    fun UCS.getEditableText() : String? {
        return this.getNode().config.getOrNull(androidx.compose.ui.semantics.SemanticsProperties.EditableText)?.text
    }

    fun UCS.clickRightIcon() : UCS = this.clickCenterRight()

    fun UCS.tapBackspace(times: Int = 1) : UCS {
        val text = this.getEditableText() ?: ""
        return this.setText(text.dropLast(times))
    }

    fun UCS.tapValidChar(times: Int = 1) : UCS = this
        .typeText(config.validChar.times(times))
}