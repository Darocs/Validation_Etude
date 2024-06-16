package ru.polescanner.validation_etude.ui.signin

import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.printToLog
import com.atiurin.ultron.core.compose.createDefaultUltronComposeRule
import com.atiurin.ultron.extensions.assertIsDisplayed
import com.atiurin.ultron.page.Screen
import org.junit.Rule
import org.junit.Test

class SignInTest {
    
    @get:Rule
    val composeRule = createDefaultUltronComposeRule()
    
    @Test
    fun signInRoute() {
        composeRule.setContent { 
            SignInRoute()
        }
        SignInScreen {
            loginField.assertIsDisplayed().assertTextContains("Login")
            //loginField.inputText("A").assert
        }
        composeRule.onRoot().printToLog("My_TAG")



    }
}





object SignInScreen : Screen<SignInScreen>(){
    val loginField = hasText("Login")
    val passwordField = hasText("Password")
    val submitButton = hasText("Submit")
    val loginPlaceholder = hasContentDescription("placeholder")
}