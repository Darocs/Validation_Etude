package ru.polescanner.validation_etude.ui.reusable.components

import com.atiurin.ultron.core.compose.createDefaultUltronComposeRule
import org.junit.Rule
import org.junit.Test

class TextFieldKtTest {

    @get:Rule
    val composeRule = createDefaultUltronComposeRule()

    @Test
    fun validatedOutlinedTextField() {

        composeRule.setContent {
        }
    }
}