package ru.polescanner.validation_etude.ui.signin.extensions

import com.atiurin.ultron.core.compose.nodeinteraction.UltronComposeSemanticsNodeInteraction
import com.atiurin.ultron.core.compose.nodeinteraction.clickCenterRight
import ru.polescanner.validation_etude.domain.general.DI

object LoginExtensions {
    fun UCS.loginInvalidMinChars(): UCS = this.setText("1")
        .assertTextContains("1")
        .assertTextContains("Min 2 chars")
        .assertTextContains("Login*")

    fun UCS.loginInvalidMaxChars(): UCS = this.setText("123456")
        .assertTextContains("123456")
        .assertTextContains("Max 5 chars")
        .assertTextContains("Login*")

    fun UCS.loginInvalidRegex(): UCS = this.setText("A")
        .assertTextContains("A")
        .assertTextContains("Min 2 chars")
        .assertTextContains("Login*")
        .setText("1B")
        .assertTextContains("Allowed chars: ${DI.login?.regex}")
        .setText("ABCDEF")
        .assertTextContains("Max 5 chars")

    fun UCS.loginIsValid(): UCS = this.setText("12")
        .assertTextContains("12")
        .assertTextContains("e.g. Darocs")
        .assertTextContains("Login")
}

object PasswordExtensions {
    fun UCS.passwordInvalidMinChars(): UCS = this.setText("AB")
        .assertTextContains("••")
        .clickCenterRight()
        .assertTextContains("AB")
        .assertTextContains("Min 3 chars")
        .assertTextContains("Password*")

    fun UCS.passwordInvalidMaxChars(): UCS = this.setText("ABCDEFG")
        .assertTextContains("Max 6 chars")
        .assertTextContains("Password*")

    fun UCS.passwordInvalidRegex(): UCS = this.setText("1")
        .assertTextContains("Min 3 chars")
        .assertTextContains("Password*")
        .setText("AB3")
        .assertTextContains("Allowed chars: ${DI.password?.regex}")
        .setText("1234567")
        .assertTextContains("Max 6 chars")

    fun UCS.passwordIsValid(): UCS = this.setText("ABC")
        .assertTextContains("ABC")
        .assertTextContains("Password")
}

typealias UCS = UltronComposeSemanticsNodeInteraction
