package ru.polescanner.validation_etude.ui.signin

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.test.hasContentDescription
import com.atiurin.ultron.core.compose.createDefaultUltronComposeRule
import com.atiurin.ultron.core.compose.nodeinteraction.click
import com.atiurin.ultron.extensions.assertIsDisplayed
import com.atiurin.ultron.extensions.assertTextContains
import com.atiurin.ultron.extensions.click
import com.atiurin.ultron.extensions.clickCenterRight
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
import ru.polescanner.validation_etude.ui.signin.SignInScreen.loginField
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

class SignInTest {
    
    @get:Rule
    val composeRule = createDefaultUltronComposeRule()

    @Before
    fun setUp() {
        //given
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
        // given
        val loginLabel = "Login"
        val errorLoginLabel = "Login*"

        val supportingText = "e.g. Darocs"
        val minCharError = "Min ${login?.min} chars"
        val maxCharError = "Max ${login?.max} chars"
        val regexError = "Allowed chars: ${login?.regex}"

        val emptyText = ""
        val validMinChar = "1"
        val invalMinChar = "A"

        val invalRegex = "AC"

        // from okay to okay0
        loginField.clickCenterRight().assertTextContains(loginLabel)
            .assertTextContains(emptyText)
            .assertTextContains(supportingText)
            .assertStateDescriptionContains("valid")
        loginField.click().assertTextContains(loginLabel)
            .assertTextContains(emptyText.dropLast(1))
            .assertTextContains(supportingText)
            .assertStateDescriptionContains("valid")

        // from okay0 to min
        loginField.assertTextContains(emptyText).setText(validMinChar)
            .assertTextContains(errorLoginLabel)
            .assertTextContains(validMinChar)
            .assertTextContains(minCharError)
            .assertStateDescriptionContains("invalid")
            .setText(validMinChar.dropLast(1)).assertTextContains(emptyText)
            .assertStateDescriptionContains("valid")
        loginField.assertTextContains(emptyText).setText(invalMinChar)
            .assertTextContains(errorLoginLabel)
            .assertTextContains(invalMinChar)
            .assertTextContains(minCharError)
            .assertStateDescriptionContains("invalid")
            .setText(invalMinChar + "C")
            .assertTextContains(errorLoginLabel)
            .assertTextContains(invalRegex)
            .assertTextContains(regexError)
            .assertStateDescriptionContains("invalid")
            .setText(invalMinChar.dropLast(1)).assertTextContains(emptyText)
            .assertStateDescriptionContains("valid")
    }
}

object SignInScreen : Screen<SignInScreen>(){
    val loginField = hasContentDescription("loginTag")
    val passwordField = hasContentDescription("password")
    val rememberMe = hasContentDescription("checkBox")
    val submitButton = hasContentDescription("submit")

    fun checkLogin() {
        loginField.assertTextContains("Login")
            .assertTextContains("")
            .loginInvalidMinChars()
            .loginInvalidMaxChars()
            .loginInvalidRegex()
            .loginIsValid()
    }

    fun checkPassword() {
        passwordField.assertTextContains("Password")
            .assertTextContains("")
            .passwordInvalidMinChars()
            .passwordInvalidMaxChars()
            .passwordInvalidRegex()
            .passwordIsValid()
    }

    fun checkRememberMe() {
        rememberMe.assertTextContains("Remember me")
            .assertHasClickAction()
            .assertIsIndeterminate()
            .click()
            .assertIsOn()
            .click()
            .assertIsOff()
    }
}
