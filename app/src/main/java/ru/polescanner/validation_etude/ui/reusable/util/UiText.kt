package ru.polescanner.validation_etude.ui.reusable.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource

//taken from https://stackoverflow.com/questions/74044246/how-to-get-stringresource-if-not-in-a-composable-function
/*
Inside your ViewModel where you can simply supply the id of the string resource

var stringResourceVariable = UiText.StringResource(R.string.some_string_resource)
Then in your Composable

viewModel.stringResourceVariable.asString()
you can simply invoke asString and it will be resolved depending on what type of UiText string you supplied.

The benefit of having this approach, is you won't have any problems supplying string resources in your ViewModel.
 */
sealed interface UiText {

    data class Str(val value: String) : UiText

    class Res(
        @androidx.annotation.StringRes val resourceId: Int,
        vararg val args: Any
    ) : UiText

    class Plural(
        @androidx.annotation.PluralsRes val resourceId: Int,
        val count: Int,
        vararg val args: Any
    ): UiText
    
    class Concat(
        val list: List<UiText>
    ): UiText
    
    @Composable
    fun asString(): String {
        return when (this) {
            is Str -> {
                value
            }

            is Res -> {
                stringResource(
                    id = resourceId,
                    formatArgs = args
                )
            }

            is Plural -> {
                pluralStringResource(
                    id = resourceId,
                    count = count,
                    formatArgs = args
                )
            }
            
            is Concat -> list.map { it.asString() }.reduce { acc, str -> acc + str }
        }
    }
    operator fun plus(other: UiText): UiText = Concat(listOf(this, other))
}
