package ru.polescanner.validation_etude.ui.signin.extensions

import com.atiurin.ultron.core.compose.nodeinteraction.UltronComposeSemanticsNodeInteraction
import ru.polescanner.validation_etude.domain.general.DI

object LoginExtensions {
    fun UCS.invalidLoginRegex(): UCS = this.setText("A")
        .assertTextContains("A")
        .assertTextContains("Min 2 chars")
        .assertTextContains("Login*")
        .setText("AB")
        .assertTextContains("Allowed chars: ${DI.login?.regex}")
        .setText("ABCDEF")
        .assertTextContains("Max 5 chars")

    fun UCS.invalidMinChars(): UCS = this.setText("1")
        .assertTextContains("1")
        .assertTextContains("Min 2 chars")
        .assertTextContains("Login*")

    fun UCS.invalidMaxChars(): UCS = this.setText("123456")
        .assertTextContains("123456")
        .assertTextContains("Max 5 chars")
        .assertTextContains("Login*")

    fun UCS.validLogin(): UCS = this.setText("123")
        .assertTextContains("123")
        .assertTextContains("e.g. Darocs")
        .assertTextContains("Login")
}

typealias UCS = UltronComposeSemanticsNodeInteraction
