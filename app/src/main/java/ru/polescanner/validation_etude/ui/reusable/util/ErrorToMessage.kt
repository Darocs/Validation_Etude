package ru.polescanner.validation_etude.ui.reusable.util

import ru.polescanner.validation_etude.R
import ru.polescanner.validation_etude.domain.general.ErrorType
import ru.polescanner.validation_etude.domain.general.Name
import ru.polescanner.validation_etude.domain.general.Name.ValidationError.ExceedMaxChar
import ru.polescanner.validation_etude.domain.general.Name.ValidationError.NotTrimmed
import ru.polescanner.validation_etude.domain.general.Name.ValidationError.ShortMinChar
import ru.polescanner.validation_etude.domain.general.Name.ValidationError.ViolationAllowedChars

fun <E : ErrorType> E.errorToMessage(): UiText =
    when (this) {
        NotTrimmed -> UiText.Res(R.string.not_trimmed)

        is ExceedMaxChar -> UiText.Res(R.string.is_too_long) + UiText.Str(" ${this.max} ") + UiText.Res(
            R.string.chars
        )

        is ShortMinChar -> UiText.Res(R.string.not_enough_chars) + UiText.Str(" ${this.min} ") + UiText.Res(
            R.string.chars
        )

        is ViolationAllowedChars -> UiText.Res(R.string.not_allowed_chars) + UiText.Str(": ${this.regex}")

        else -> UiText.Res(R.string.smth_get_wrong)
    }

fun Name.ValidationError.toMessage(): UiText =
    when (this) {
        NotTrimmed -> UiText.Res(R.string.not_trimmed)

        is ExceedMaxChar -> UiText.Res(R.string.is_too_long) + UiText.Str(" ${this.max} ") + UiText.Res(
            R.string.chars
        )

        is ShortMinChar -> UiText.Res(R.string.not_enough_chars) + UiText.Str(" ${this.min} ") + UiText.Res(
            R.string.chars
        )

        is ViolationAllowedChars -> UiText.Res(R.string.not_allowed_chars) + UiText.Str(": ${this.regex}")

        else -> UiText.Res(R.string.smth_get_wrong)
    }

fun ErrorType.toMessage(): UiText = when(this) {
    is Name.ValidationError -> this.toMessage()

    else -> TODO()
}