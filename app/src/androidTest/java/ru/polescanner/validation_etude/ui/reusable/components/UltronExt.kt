package ru.polescanner.validation_etude.ui.reusable.components

import androidx.compose.ui.semantics.getOrNull
import com.atiurin.ultron.core.compose.nodeinteraction.clickCenterRight
import ru.polescanner.validation_etude.ui.signin.extensions.UCS

object TextField {
    fun UCS.getEditableText() : String? {
        return this.getNode().config.getOrNull(androidx.compose.ui.semantics.SemanticsProperties.EditableText)?.text
    }

    fun UCS.clickRightIcon() : UCS = this.clickCenterRight()
}