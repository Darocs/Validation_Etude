package ru.polescanner.validation_etude.ui.signin.extensions

import com.atiurin.ultron.core.compose.nodeinteraction.UltronComposeSemanticsNodeInteraction
import com.atiurin.ultron.core.compose.nodeinteraction.clickCenterRight
import ru.polescanner.validation_etude.domain.general.DI.login
import ru.polescanner.validation_etude.domain.general.DI.password
import ru.polescanner.validation_etude.ui.reusable.components.AssertStateDescription.assertStateDescriptionContains

object LoginExtensions {
    fun UCS.loginInvalidMinChars(): UCS = this.setText("1")
        .assertTextContains("1")
        .assertTextContains("Min ${login?.min} chars")
        .assertTextContains("Login*")
        .assertStateDescriptionContains("invalid")

    fun UCS.loginInvalidMaxChars(): UCS = this.setText("1234")
        .assertTextContains("1234")
        .assertTextContains("Max ${login?.max} chars")
        .assertStateDescriptionContains("invalid")

    fun UCS.loginInvalidRegex(): UCS = this.setText("A")
        .assertTextContains("A")
        .assertTextContains("Min ${login?.min} chars")
        .assertStateDescriptionContains("invalid")
        .setText("1B")
        .assertTextContains("Allowed chars: ${login?.regex}")
        .assertStateDescriptionContains("invalid")
        .setText("ABCD")
        .assertTextContains("Max ${login?.max} chars")
        .assertStateDescriptionContains("invalid")

    fun UCS.loginIsValid(): UCS = this.setText("12")
        .assertTextContains("12")
        .assertTextContains("e.g. Darocs")
        .assertTextContains("Login")
        .assertStateDescriptionContains("valid")
}

object PasswordExtensions {
    fun UCS.passwordInvalidMinChars(): UCS = this.setText("AB")
        .assertTextContains("••")
        .clickCenterRight()
        .assertTextContains("AB")
        .assertTextContains("Min ${password?.min} chars")
        .assertTextContains("Password*")
        .assertStateDescriptionContains("invalid")

    fun UCS.passwordInvalidMaxChars(): UCS = this.setText("ABCDEFG")
        .assertTextContains("Max ${password?.max} chars")
        .assertStateDescriptionContains("invalid")

    fun UCS.passwordInvalidRegex(): UCS = this.setText("1")
        .assertTextContains("Min ${password?.min} chars")
        .assertStateDescriptionContains("invalid")
        .setText("AB3")
        .assertTextContains("Allowed chars: ${password?.regex}")
        .assertStateDescriptionContains("invalid")
        .setText("1234567")
        .assertTextContains("Max ${password?.max} chars")
        .assertStateDescriptionContains("invalid")

    fun UCS.passwordIsValid(): UCS = this.setText("ABC")
        .assertTextContains("ABC")
        .assertTextContains("Password")
        .assertStateDescriptionContains("valid")
}

typealias UCS = UltronComposeSemanticsNodeInteraction
