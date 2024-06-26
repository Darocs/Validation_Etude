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
import ru.polescanner.validation_etude.ui.signin.extensions.CommonExtensions.assertIsMax
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

    // Inval2 tests
    @Test
    fun `on inval2 delete valid character`() {
        val inval2 = loginField.click().tapInvalChar().tapValidChar().assertIsInval2()
        inval2.tapBackspace().assertIsMin(valid = false) // inval - valid = min(inval)
    }

    @Test
    fun `on inval2 delete invalid character`() {
        val inval2 = loginField.click().tapInvalChar(2).assertIsInval2()
        inval2.tapBackspace().assertIsMin(valid = false)
    }

    @Test
    fun `on inval2 tap valid character`() {
        val inval2 = loginField.click().tapInvalChar(2).assertIsInval2()
        inval2.tapValidChar().assertIsInval3()
    }

    @Test
    fun `on inval2 tap invalid character`() {
        val inval2 = loginField.click().tapInvalChar(2).assertIsInval2()
        inval2.tapInvalChar().assertIsInval3()
    }

    // Okay3 tests
    @Test
    fun `on ok3 click right icon`() {
        val ok3 = loginField.click().tapValidChar(3).assertIsOkay3()
        ok3.clickRightIcon().assertIsOkay0()
    }

    @Test
    fun `on ok3 tap backspace`() {
        val ok3 = loginField.click().tapValidChar(3).assertIsOkay3()
        ok3.tapBackspace().assertIsOkay2()
    }

    @Test
    fun `on ok3 tap valid character`() {
        val ok3 = loginField.click().tapValidChar(3).assertIsOkay3()
        ok3.tapValidChar().assertIsMax()
    }

    @Test
    fun `on ok3 tap invalid character`() {
        val ok3 = loginField.click().tapValidChar(3).assertIsOkay3()
        ok3.tapInvalChar().assertIsMax()
    }

    // Inval3 tests
    @Test
    fun `on inval3 delete valid character to inval2`() {
        val inval3 = loginField.click().tapInvalChar(2).tapValidChar().assertIsInval3()
        inval3.tapBackspace().assertIsInval2()
    }

    @Test
    fun `on inval3 delete inval character to ok2`() {
        val inval3 = loginField.click().tapValidChar(2).tapInvalChar(1).assertIsInval3()
        inval3.tapBackspace().assertIsOkay2()
    }

    @Test
    fun `on inval3 delete invalid character to inval2`() {
        val inval3 = loginField.click().tapInvalChar(3).assertIsInval3()
        inval3.tapBackspace().assertIsInval2()
    }

    @Test
    fun `on inval3 tap valid character`() {
        val inval3 = loginField.click().tapInvalChar(3).assertIsInval3()
        inval3.tapValidChar().assertIsMax()
    }

    @Test
    fun `on inval3 tap invalid character`() {
        val inval3 = loginField.click().tapInvalChar(3).assertIsInval3()
        inval3.tapInvalChar().assertIsMax()
    }

    // Max tests
    @Test
    fun `on max4 delete valid character to ok3`() {
        val max = loginField.click().tapValidChar(4).assertIsMax()
        max.tapBackspace().assertIsOkay3()
    }

    @Test
    fun `on max4 delete valid character to inval3`() {
        val max = loginField.click().tapInvalChar(3).tapValidChar().assertIsMax()
        max.tapBackspace().assertIsInval3()
    }

    @Test
    fun `on max5 delete valid character`() {
        val max = loginField.click().tapValidChar(5).assertIsMax()
        max.tapBackspace().assertIsMax()
    }

    @Test
    fun `on max4 delete invalid character to ok3`() {
        val max = loginField.click().tapValidChar(3).tapInvalChar().assertIsMax()
        max.tapBackspace().assertIsOkay3()
    }

    @Test
    fun `on max4 delete invalid character to inval3`() {
        val max = loginField.click().tapInvalChar(4).assertIsMax()
        max.tapBackspace().assertIsInval3()
    }

    @Test
    fun `on max5 delete invalid character`() {
        val max = loginField.click().tapInvalChar(5).assertIsMax()
        max.tapBackspace().assertIsMax()
    }

    @Test
    fun `on max4 tap valid character`() {
        val max = loginField.click().tapValidChar(4).assertIsMax()
        max.tapValidChar().assertIsMax()
    }

    @Test
    fun `on max4 tap invalid character`() {
        val max = loginField.click().tapValidChar(4).assertIsMax()
        max.tapInvalChar().assertIsMax()
    }
}

object SignInScreen : Screen<SignInScreen>(){
    val loginField = hasContentDescription("loginTag")
    val passwordField = hasContentDescription("password")
    val rememberMe = hasContentDescription("checkBox")
    val submitButton = hasContentDescription("submit")
}

