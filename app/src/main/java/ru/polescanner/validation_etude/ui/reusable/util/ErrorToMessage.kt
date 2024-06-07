package ru.polescanner.validation_etude.ui.reusable.util

import ru.polescanner.validation_etude.R
import ru.polescanner.validation_etude.domain.general.ConductorCount
import ru.polescanner.validation_etude.domain.general.ErrorType
import ru.polescanner.validation_etude.domain.general.Name
import ru.polescanner.validation_etude.domain.general.Name.ValidationError.ExceedMaxChar
import ru.polescanner.validation_etude.domain.general.Name.ValidationError.NotTrimmed
import ru.polescanner.validation_etude.domain.general.Name.ValidationError.ShortMinChar
import ru.polescanner.validation_etude.domain.general.Name.ValidationError.ViolationAllowedChars

fun <E : ErrorType> E.errorToMessage(): UiText =
    when (this) {
        NotTrimmed -> TODO()
        is ExceedMaxChar -> UiText.Res(R.string.is_too_long) + UiText.Str(" ${this.max} ") + UiText.Res(
            R.string.app_name
        ) // TODO: Add res

        is ShortMinChar -> UiText.Res(R.string.not_enough_chars) + UiText.Str(" ${this.min} ") + UiText.Res(
            R.string.app_name
        ) // TODO: Add res

        is ViolationAllowedChars -> TODO()

        else -> TODO()
    }

fun Name.ValidationError.toMessage(): UiText =
    when (this) {
        NotTrimmed -> TODO()
        is ExceedMaxChar -> UiText.Res(R.string.is_too_long) + UiText.Str(" ${this.max} ") + UiText.Res(
            R.string.app_name
        ) // TODO: Add res

        is ShortMinChar -> UiText.Res(R.string.not_enough_chars) + UiText.Str(" ${this.min} ") + UiText.Res(
            R.string.app_name
        ) // TODO: Add res

        is ViolationAllowedChars -> TODO()
        ConductorCount.ValidationError.Negative -> TODO()
    }

fun ErrorType.toMessage(): UiText = when(this) {
    is Name.ValidationError -> this.toMessage()

    else -> TODO()
}