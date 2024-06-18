package ru.polescanner.validation_etude.ui.signin

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.test.hasContentDescription
import com.atiurin.ultron.core.compose.createDefaultUltronComposeRule
import com.atiurin.ultron.extensions.assertTextContains
import com.atiurin.ultron.page.Screen
import org.junit.Rule
import org.junit.Test
import ru.polescanner.validation_etude.LocalSnackbarHostState
import ru.polescanner.validation_etude.domain.general.DI
import ru.polescanner.validation_etude.domain.general.NameRules
import ru.polescanner.validation_etude.ui.signin.extensions.LoginExtensions.invalidLoginRegex
import ru.polescanner.validation_etude.ui.signin.extensions.LoginExtensions.invalidMaxChars
import ru.polescanner.validation_etude.ui.signin.extensions.LoginExtensions.invalidMinChars
import ru.polescanner.validation_etude.ui.signin.extensions.LoginExtensions.validLogin

class SignInTest {
    
    @get:Rule
    val composeRule = createDefaultUltronComposeRule()

    @Test
    fun signInRoute() {
        DI.login = NameRules(2, 5, "[1-9]+")
        DI.password = NameRules(2, 5, "[1-9]+")

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
        }

/*
        composeRule.onRoot().printToLog("My_TAG")
*/
    }
}

object SignInScreen : Screen<SignInScreen>(){

    val loginField = hasContentDescription("myTextFieldTag")
    val passwordField = hasContentDescription("password")
    val rememberMe = hasContentDescription("checkBox")
    val submitButton = hasContentDescription("submit")

    fun checkLogin() {
        loginField.assertTextContains("Login")
            .assertTextContains("")
            .invalidLoginRegex()
            .invalidMinChars()
            .invalidMaxChars()
            .validLogin()
    }
}
