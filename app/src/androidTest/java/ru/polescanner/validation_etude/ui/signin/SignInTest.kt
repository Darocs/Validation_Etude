package ru.polescanner.validation_etude.ui.signin

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.atiurin.ultron.core.compose.createDefaultUltronComposeRule
import com.atiurin.ultron.core.compose.nodeinteraction.click
import com.atiurin.ultron.extensions.assertTextContains
import com.atiurin.ultron.page.Screen
import org.junit.Rule
import org.junit.Test
import ru.polescanner.validation_etude.LocalSnackbarHostState
import ru.polescanner.validation_etude.domain.general.DI
import ru.polescanner.validation_etude.domain.general.NameRules
import ru.polescanner.validation_etude.ui.reusable.components.AssertCheckBox.assertIsIndeterminate
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

    @Test
    fun signInRoute() {
        //given
        DI.login = NameRules(2, 5, "[1-9]+")
        DI.password = NameRules(3, 6, "[a-zA-Z]+")

        composeRule.setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            CompositionLocalProvider(
                LocalSnackbarHostState provides snackbarHostState,
            ) {
                SignInRoute()
            }
        }

        SignInScreen {
            checkLogin()
            checkPassword()
            checkRememberMe()
        }

        composeRule.onRoot().printToLog("My_TAG")
    }
}

object SignInScreen : Screen<SignInScreen>(){
    private val loginField = hasContentDescription("loginTag")
    private val passwordField = hasContentDescription("password")
    private val rememberMe = hasContentDescription("checkBox")
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
