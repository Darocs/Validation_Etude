package ru.polescanner.validation_etude.domain.general

import arrow.core.Either
import arrow.core.left
import arrow.core.raise.either
import arrow.core.raise.ensure
import arrow.core.right
import ru.polescanner.validation_etude.domain.general.Name.ValidationError
import ru.polescanner.validation_etude.domain.general.Name.ValidationError.ExceedMaxChar
import ru.polescanner.validation_etude.domain.general.Name.ValidationError.NotTrimmed
import ru.polescanner.validation_etude.domain.general.Name.ValidationError.ShortMinChar
import ru.polescanner.validation_etude.domain.general.Name.ValidationError.ViolationAllowedChars

// ToDo Unfortunately not only Domain Primitives are to be validated, but Boolean? as well,
//  so I refactor parseOrPrompt to <D: Any>
interface DomainPrimitive

interface ErrorType

interface Name : DomainPrimitive {
    val value: String

    sealed interface ValidationError : ErrorType {
        data object NotTrimmed : ValidationError
        data class ShortMinChar(val min: Int) : ValidationError
        data class ExceedMaxChar(val max: Int) : ValidationError
        data class ViolationAllowedChars(val regex: String) : ValidationError
    }
}

data class ConductorCount(val count: Int) : DomainPrimitive {
    sealed interface ValidationError : ErrorType {
        data object Negative : Name.ValidationError
        data class TooSmall(val min: Int) : ValidationError


    }
}

private fun <V : Name> V.validated(
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

fun <E: ErrorType, V> Either<E, V>.orThrow() : V = when (this) {
    is Either.Left -> throw IllegalArgumentException() // TODO: Define correct exception
    is Either.Right -> value
}

// ToDo Module is compiled how to provide constants in runtime
@JvmInline
value class Login private constructor(override val value: String) : Name {
    companion object {
        operator fun invoke(login: String): Either<ValidationError, Login> = Login(login.trim())
            .validated(
                minLen = 5,
                maxLen = 10,
                validCharsRegex = "[a-zA-Z0-9]+"
            )
    }
}

@JvmInline
value class Password private constructor(override val value: String) : Name {
    companion object {
        operator fun invoke(password: String): Either<ValidationError, Password> =
            Password(password.trim())
                .validated(
                    minLen = 5,
                    maxLen = 10,
                    validCharsRegex = "[a-zA-Z0-9]+"
                )
    }
}

fun String.toLogin() = Login(this)
fun String.toPassword() = Password(this)
fun Boolean?.check(): Either<ErrorType, Boolean> = this?.right() ?: BooleanValidationError.left()
data object BooleanValidationError: ErrorType