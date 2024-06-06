package ru.polescanner.validation_etude.ui.reusable.util

import ru.polescanner.validation_etude.ui.reusable.kotlinapi.readInstanceProperty
import kotlin.reflect.full.createType
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


typealias Focusable<E> = ValidOrFocusedAtCheck<E>

data class ValidOrFocusedAtCheck<E> (
    val value: E,
    val isFocused: Boolean
) {
    fun setFocus(): ValidOrFocusedAtCheck<E> = this.copy(isFocused = true)

    fun clearFocus(): ValidOrFocusedAtCheck<E> = this.copy(isFocused = false)
}
fun <T: Any?> T?.toNullableFocusable(): Focusable<T?> =
    this?.let{ Focusable(it, false) } ?: Focusable(null, false)

fun <T: Any> T.toFocusable(): Focusable<T> = Focusable(this, false)

fun atMostOneFocused(vararg elements: ValidOrFocusedAtCheck<out Any?>): Boolean =
    elements.count{it.isFocused} <= 1


/**
 * Reflection version to check invariance that only one view element isFocused in the moment
 * for [ValidOrFocusedAtCheck] properties of the [UiState] data class instance
 * Properties of other types, non-ValidOrFocusedAtCheck, not supported
 *
 * requires: kotlin reflection
 *
 * @param [instance] instance of the UiState data class
 * @throws [IllegalArgumentException] if property of other than [ValidOrFocusedAtCheck] type
 */
fun <C: Any> atMostOneFocused(instance: C): Boolean {
    val kClass = instance::class
    require(kClass.isData) { "only data classes instances are supported" }
    val propertyNames = kClass.primaryConstructor!!.parameters.map { it.name!! }
    val propertyValues = propertyNames.map { name ->
        when (val type = kClass.memberProperties.find {it.name == name}!!.returnType) {
            ValidOrFocusedAtCheck::class.createType(type.arguments) ->
                readInstanceProperty<ValidOrFocusedAtCheck<*>>(instance, name)
            else -> throw IllegalArgumentException(
                "not applicable type of the field. Only ${ValidOrFocusedAtCheck::class.simpleName} supported"
            )
        }
    }
    return propertyValues.count { it.isFocused } <= 1
}

