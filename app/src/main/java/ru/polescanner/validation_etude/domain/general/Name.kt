package ru.polescanner.validation_etude.domain.general

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure
import ru.polescanner.validation_etude.domain.general.Name.ValidationError
import ru.polescanner.validation_etude.domain.general.Name.ValidationError.ExceedMaxChar
import ru.polescanner.validation_etude.domain.general.Name.ValidationError.NotTrimmed
import ru.polescanner.validation_etude.domain.general.Name.ValidationError.ShortMinChar
import ru.polescanner.validation_etude.domain.general.Name.ValidationError.ViolationAllowedChars

interface Name {
    val value: String
    sealed interface ValidationError : VOVErr {
        data object NotTrimmed : ValidationError
        data class ShortMinChar(val min: Int) : ValidationError
        data class ExceedMaxChar(val max: Int) : ValidationError
        data class ViolationAllowedChars(val regex: String) : ValidationError
    }
}

internal fun <V : Name> V.validated(
    minLen: Int = 0,
    maxLen: Int = 50,
    validCharsRegex: String = ".*",
    ): Either<ValidationError, V> = either {
    ensure(value == value.trim()) { NotTrimmed }
    ensure(value.length >= minLen) { ShortMinChar(minLen) }
    ensure(value.length <= maxLen) { ExceedMaxChar(maxLen) }
    ensure(Regex(validCharsRegex).matches(value)) { ViolationAllowedChars(validCharsRegex) }

    this@validated
}

fun String.toLogin() = Login(this)

fun String.toPassword() = Password(this)

data class NameRules(val min: Int, val max: Int, val regex: String)
