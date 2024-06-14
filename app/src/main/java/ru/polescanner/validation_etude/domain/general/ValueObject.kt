package ru.polescanner.validation_etude.domain.general

import arrow.core.Either
import arrow.core.left
import arrow.core.right

interface ValueObjectValidationError
typealias VOVErr = ValueObjectValidationError

fun <E : VOVErr, V : Any> Either<E, V>.orThrow(): V = when (this) {
    // TODO: Define correct exception
    is Either.Left -> error("${this::class.simpleName} violated rule. Arguments: ")
    is Either.Right -> value
}

// ToDo refactor the name!
fun Boolean?.check(): Either<VOVErr, Boolean> = this?.right() ?: BooleanValidationError.left()
data object BooleanValidationError : VOVErr
