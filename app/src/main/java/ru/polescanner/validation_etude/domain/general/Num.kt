package ru.polescanner.validation_etude.domain.general

import arrow.core.Either
import arrow.core.raise.either
import arrow.core.raise.ensure

interface Num {
    val value: Number
    sealed interface ValidationError : VOVErr {
        data class Short(val min: Int) : ValidationError
        data class Exceed(val max: Int) : ValidationError
    }
}

interface IntNum : Num {
    override val value: Int
}

interface RealNum : Num {
    override val value: Double
}

internal fun <V : Num> V.validated(
    min: Int = 0,
    max: Int = 100,
): Either<Num.ValidationError, V> = either {
    ensure(value.toDouble() >= min) { Num.ValidationError.Short(min) }
    ensure(value.toDouble() <= max) { Num.ValidationError.Exceed(max) }
    this@validated
}

data class NumRules(val min: Int, val max: Int)