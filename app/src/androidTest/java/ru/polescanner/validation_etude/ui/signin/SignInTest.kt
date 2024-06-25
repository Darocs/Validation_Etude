package ru.polescanner.validation_etude.ui.signin

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.test.hasContentDescription
import com.atiurin.ultron.core.compose.createDefaultUltronComposeRule
import com.atiurin.ultron.core.compose.nodeinteraction.click
import com.atiurin.ultron.core.compose.nodeinteraction.clickCenterRight
import com.atiurin.ultron.extensions.assertIsDisplayed
import com.atiurin.ultron.extensions.assertTextContains
import com.atiurin.ultron.extensions.click
import com.atiurin.ultron.extensions.setText
import com.atiurin.ultron.page.Screen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.polescanner.validation_etude.LocalSnackbarHostState
import ru.polescanner.validation_etude.domain.general.DI
import ru.polescanner.validation_etude.domain.general.DI.login
import ru.polescanner.validation_etude.domain.general.NameRules
import ru.polescanner.validation_etude.ui.reusable.components.AssertCheckBox.assertIsIndeterminate
import ru.polescanner.validation_etude.ui.reusable.components.AssertStateDescription.assertStateDescriptionContains
import ru.polescanner.validation_etude.ui.signin.SignInScreen.assertIsOkay0
import ru.polescanner.validation_etude.ui.signin.SignInScreen.clickRightIcon
import ru.polescanner.validation_etude.ui.signin.SignInScreen.loginField
import ru.polescanner.validation_etude.ui.signin.SignInScreen.ok0
import ru.polescanner.validation_etude.ui.signin.SignInScreen.passwordField
import ru.polescanner.validation_etude.ui.signin.SignInScreen.rememberMe
import ru.polescanner.validation_etude.ui.signin.SignInScreen.submitButton
import ru.polescanner.validation_etude.ui.signin.extensions.LoginExtensions.loginInvalidMaxChars
import ru.polescanner.validation_etude.ui.signin.extensions.LoginExtensions.loginInvalidMinChars
import ru.polescanner.validation_etude.ui.signin.extensions.LoginExtensions.loginInvalidRegex
import ru.polescanner.validation_etude.ui.signin.extensions.LoginExtensions.loginIsValid
import ru.polescanner.validation_etude.ui.signin.extensions.PasswordExtensions.passwordInvalidMaxChars
import ru.polescanner.validation_etude.ui.signin.extensions.PasswordExtensions.passwordInvalidMinChars
import ru.polescanner.validation_etude.ui.signin.extensions.PasswordExtensions.passwordInvalidRegex
import ru.polescanner.validation_etude.ui.signin.extensions.PasswordExtensions.passwordIsValid
import ru.polescanner.validation_etude.ui.signin.extensions.UCS

class SignInTest {
    
    @get:Rule
    val composeRule = createDefaultUltronComposeRule()

    @Before
    fun setUp() {
        //Given
        DI.login = NameRules(2, 3, "[1-9]+")
        DI.password = NameRules(3, 4, "[a-zA-Z]+")

        composeRule.apply {
            setContent {
                val snackbarHostState = remember { SnackbarHostState() }
                CompositionLocalProvider(
                    LocalSnackbarHostState provides snackbarHostState,
                ) {
                    SignInRoute()
                }
            }
        }
    }

    @Test
    fun signInScreenTest() {
        loginField.assertIsDisplayed().assertHasClickAction()
            .assertStateDescriptionContains("valid").assertIsNotFocused()
        passwordField.assertIsDisplayed().assertHasClickAction()
            .assertStateDescriptionContains("valid").assertIsNotFocused()
        rememberMe.assertIsDisplayed().assertHasClickAction().assertIsNotFocused()
        submitButton.assertIsDisplayed().assertHasClickAction().assertIsNotFocused()
    }

    @Test
    fun loginFieldTest() {
        // Given
        val loginLabel = "Login"
        val errorLoginLabel = "Login*"

        val supportingText = "e.g. Darocs"
        val minCharError = "Min ${login?.min} chars"
        val maxCharError = "Max ${login?.max} chars"
        val regexError = "Allowed chars: ${login?.regex}"

        val ok0Text = ""
        val ok2 = "12"
        val ok3 = "123"
        val validMinChar = "1"
        val invalRegex1 = "A"
        val invalRegex2 = "AC"
        val invalRegex3 = "ACE"

        // From ok0 to ok0
        ok0.clickRightIcon().assertIsOkay0()
        ok0.setText(ok0Text.dropLast(1)).assertIsOkay0()

        // From ok0 to min
        ok0.setText(validMinChar)
            .assertStateDescriptionContains("invalid")
            .assertTextContains(errorLoginLabel)
            .assertTextContains(validMinChar)
            .assertTextContains(minCharError)
        ok0.setText(invalRegex1)
            .assertStateDescriptionContains("invalid")
            .assertTextContains(errorLoginLabel)
            .assertTextContains(invalRegex1)
            .assertTextContains(minCharError)


        // From min to ok0/ok2/invalRegex
        loginField.setText(validMinChar).assertTextContains(validMinChar)
            .assertStateDescriptionContains("invalid")
            .setText(validMinChar.dropLast(1))
            .assertIsOkay0()
        loginField.setText(invalRegex1).assertTextContains(invalRegex1)
            .assertStateDescriptionContains("invalid")
            .setText(invalRegex1.dropLast(1))
            .assertIsOkay0()
        loginField.setText(validMinChar + "2").assertStateDescriptionContains("valid")
            .assertTextContains(loginLabel)
            .assertTextContains(ok2)
            .assertTextContains(supportingText)
        loginField.setText(invalRegex1 + "C").assertStateDescriptionContains("invalid")
            .assertTextContains(errorLoginLabel)
            .assertTextContains(invalRegex2)
            .assertTextContains(regexError) // InvalidCh + invalidCh = regexError
        loginField.setText(validMinChar + "C").assertStateDescriptionContains("invalid")
            .assertTextContains(errorLoginLabel)
            .assertTextContains(validMinChar + "C")
            .assertTextContains(regexError) // ValidCh + invalidCh = regexError

        // From ok2 to ok0/min/ok3/inval3
        loginField.setText(ok2).assertTextContains(ok2)
            .assertStateDescriptionContains("valid")
            .clickRightIcon()
            .assertIsOkay0()
        loginField.setText(ok2).assertTextContains(ok2)
            .assertStateDescriptionContains("valid")
            .setText(ok2.dropLast(1)).assertStateDescriptionContains("invalid")
            .assertTextContains(errorLoginLabel)
            .assertTextContains(validMinChar)
            .assertTextContains(minCharError)
        loginField.setText(ok2).assertTextContains(ok2)
            .assertStateDescriptionContains("valid")
            .setText(ok2 + "3")
            .assertTextContains(loginLabel)
            .assertTextContains(ok3)
            .assertTextContains(supportingText)
            .assertStateDescriptionContains("valid")
        loginField.setText(ok2 + "E").assertStateDescriptionContains("invalid")
            .assertTextContains(errorLoginLabel)
            .assertTextContains(ok2 + "E")
            .assertTextContains(regexError) // ValidCh + validCh + invalidCh = regexError
    }
}

object SignInScreen : Screen<SignInScreen>(){
    val loginField = hasContentDescription("loginTag")
    val passwordField = hasContentDescription("password")
    val rememberMe = hasContentDescription("checkBox")
    val submitButton = hasContentDescription("submit")

    val ok0 = loginField.click()

    fun UCS.assertIsOkay0() : UCS = this
        .assertTextContains("")
        .assertContentDescriptionContains("label")
        .assertContentDescriptionContains("supportingText")
        .assertStateDescriptionContains("valid")

    fun UCS.assertIsOkay2(text: String) : UCS = this
        .assertTextContains(text)
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

    fun UCS.clickRightIcon() : UCS = this.clickCenterRight()
}
