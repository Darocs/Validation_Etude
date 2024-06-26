package ru.polescanner.validation_etude.ui.signin

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.test.hasContentDescription
import com.atiurin.ultron.core.compose.createDefaultUltronComposeRule
import com.atiurin.ultron.core.compose.nodeinteraction.click
import com.atiurin.ultron.extensions.assertIsDisplayed
import com.atiurin.ultron.extensions.click
import com.atiurin.ultron.page.Screen
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.polescanner.validation_etude.LocalSnackbarHostState
import ru.polescanner.validation_etude.domain.general.DI
import ru.polescanner.validation_etude.domain.general.NameRules
import ru.polescanner.validation_etude.ui.reusable.components.AssertStateDescription.assertStateDescriptionContains
import ru.polescanner.validation_etude.ui.reusable.components.TextField.clickRightIcon
import ru.polescanner.validation_etude.ui.reusable.components.TextField.tapBackspace
import ru.polescanner.validation_etude.ui.reusable.components.TextField.tapInvalChar
import ru.polescanner.validation_etude.ui.reusable.components.TextField.tapValidChar
import ru.polescanner.validation_etude.ui.signin.SignInScreen.loginField
import ru.polescanner.validation_etude.ui.signin.SignInScreen.passwordField
import ru.polescanner.validation_etude.ui.signin.SignInScreen.rememberMe
import ru.polescanner.validation_etude.ui.signin.SignInScreen.submitButton
import ru.polescanner.validation_etude.ui.signin.extensions.CommonExtensions.assertIsInval3
import ru.polescanner.validation_etude.ui.signin.extensions.CommonExtensions.assertIsMin
import ru.polescanner.validation_etude.ui.signin.extensions.CommonExtensions.assertIsOkay0
import ru.polescanner.validation_etude.ui.signin.extensions.CommonExtensions.assertIsOkay3
import ru.polescanner.validation_etude.ui.signin.extensions.CommonExtensions.assertIsStart
import ru.polescanner.validation_etude.ui.signin.extensions.LoginExtensions.assertIsInval2
import ru.polescanner.validation_etude.ui.signin.extensions.LoginExtensions.assertIsOkay2

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

    // Screen tests
    @Test
    fun signInScreenTest() {
        loginField.assertIsDisplayed().assertHasClickAction()
            .assertStateDescriptionContains("valid").assertIsNotFocused()
        passwordField.assertIsDisplayed().assertHasClickAction()
            .assertStateDescriptionContains("valid").assertIsNotFocused()
        rememberMe.assertIsDisplayed().assertHasClickAction().assertIsNotFocused()
        submitButton.assertIsDisplayed().assertHasClickAction().assertIsNotFocused()
    }

    // Start tests
    @Test
    fun `from start click on right icon`() {
        val start = loginField.assertIsDisplayed()
        start.clickRightIcon().assertIsStart()
    }

    @Test
    fun `from start click on loginField`() {
        val start = loginField.assertIsDisplayed()
        start.click().assertIsOkay0()
    }

    // Okay0 tests
    @Test
    fun `on ok0 click right icon`() {
        val ok0 = loginField.click().assertIsOkay0()
        ok0.clickRightIcon().assertIsOkay0()
    }

    @Test
    fun `on ok0 tap backspace`() {
        val ok0 = loginField.click().assertIsOkay0()
        ok0.tapBackspace().assertIsOkay0()
    }

    @Test
    fun `on ok0 tap valid character`() {
        val ok0 = loginField.click().assertIsOkay0()
        ok0.tapValidChar().assertIsMin(valid = true)
    }

    @Test
    fun `on ok0 tap invalid character`() {
        val ok0 = loginField.click().assertIsOkay0()
        ok0.tapInvalChar().assertIsMin(valid = false)
    }

    // Min tests
    @Test
    fun `on valid min tap backspace`() {
        val min = loginField.click().tapValidChar().assertIsMin(valid = true)
        min.tapBackspace().assertIsOkay0()
    }

    @Test
    fun `on invalid min tap backspace`() {
        val min = loginField.click().tapInvalChar().assertIsMin(valid = false)
        min.tapBackspace().assertIsOkay0()
    }

    @Test
    fun `on valid min tap valid character`() {
        val min = loginField.click().tapValidChar().assertIsMin(valid = true)
        min.tapValidChar().assertIsOkay2()
    }

    @Test
    fun `on valid min tap invalid character`() {
        val min = loginField.click().tapValidChar().assertIsMin(valid = true)
        min.tapInvalChar().assertIsInval2() // ValidCh + invalidCh = regexError
    }

    @Test
    fun `on invalid min tap invalid character`() {
        val min = loginField.click().tapInvalChar().assertIsMin(valid = false)
        min.tapInvalChar().assertIsInval2() // InvalidCh + invalidCh = regexError
    }

    @Test
    fun `on invalid min tap valid character`() {
        val min = loginField.click().tapValidChar().assertIsMin(valid = true)
        min.tapInvalChar().assertIsInval2() // InvalidCh + validCh = regexError
    }

    // Okay2 tests
    @Test
    fun `on ok2 click right icon`() {
        val ok2 = loginField.click().tapValidChar(2).assertIsOkay2()
        ok2.clickRightIcon().assertIsOkay0()
    }

    @Test
    fun `on ok2 tap backspace`() {
        val ok2 = loginField.click().tapValidChar(2).assertIsOkay2()
        ok2.tapBackspace().assertIsMin(valid = true)
    }

    @Test
    fun `on ok2 tap valid character`() {
        val ok2 = loginField.click().tapValidChar(2).assertIsOkay2()
        ok2.tapValidChar().assertIsOkay3()
    }

    @Test
    fun `on ok2 tap invalid character`() {
        val ok2 = loginField.click().tapValidChar(2).assertIsOkay2()
        ok2.tapInvalChar().assertIsInval3()
    }
}

object SignInScreen : Screen<SignInScreen>(){
    val loginField = hasContentDescription("loginTag")
    val passwordField = hasContentDescription("password")
    val rememberMe = hasContentDescription("checkBox")
    val submitButton = hasContentDescription("submit")
}

