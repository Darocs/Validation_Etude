package ru.polescanner.validation_etude.ui.reusable.util

import arrow.core.Either
import ru.polescanner.validation_etude.domain.general.ErrorType
import ru.polescanner.validation_etude.ui.reusable.kotlinapi.constructorProperties
import ru.polescanner.validation_etude.ui.reusable.kotlinapi.copyDataObject
import ru.polescanner.validation_etude.ui.reusable.kotlinapi.readInstanceProperty
import kotlin.jvm.internal.CallableReference
import kotlin.reflect.KProperty0
import kotlin.reflect.full.createType
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.isSubtypeOf
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
fun <T: Any?> T?.withClearedFocusForNullable(): Focusable<T?> =
    this?.let{ Focusable(it, false) } ?: Focusable(null, false)

fun <T: Any> T.withClearedFocus(): Focusable<T> = Focusable(this, false)

fun atMostOneFocused(vararg elements: ValidOrFocusedAtCheck<out Any?>): Boolean =
    elements.count{it.isFocused} <= 1


/**
 * Reflection version to check invariance that only one view element isFocused in the moment
 * for [ValidOrFocusedAtCheck] properties of the [UiState] data class instance.
 * Properties of other types, non-ValidOrFocusedAtCheck, not supported
 *
 * requires: kotlin reflection
 *
 * @param [instance] instance of the UiState data class
 * @throws [IllegalArgumentException] if property of other than [ValidOrFocusedAtCheck] type
 */
fun <U: UiState> U.atMostOneFocused(): Boolean {
    val kClass = this::class
    require(kClass.isData) { "only data classes instances are supported" }
    //ToDo filter only Focusable
    val propertyNames = kClass.primaryConstructor!!.parameters.filter{ it == it }.map { it.name!! }
    val propertyValues = propertyNames.map { name ->
        when (val type = kClass.memberProperties.find {it.name == name}!!.returnType) {
            ValidOrFocusedAtCheck::class.createType(type.arguments) ->
                readInstanceProperty<ValidOrFocusedAtCheck<*>>(this, name)
            else -> throw IllegalArgumentException(
                "not applicable type of the field. Only ${ValidOrFocusedAtCheck::class.simpleName} supported"
            )
        }
    }
    return propertyValues.count { it.isFocused } <= 1
}

//https://stackoverflow.com/questions/43822920/kotlin-check-if-function-requires-instance-parameter
@Suppress("UNCHECKED_CAST")
fun <E, D: Any> KProperty0<Focusable<E>>.parseOrPrompt(
    deliver: (UiText) -> Unit,
    parse: (E) -> Either<ErrorType, D>
): Either<UiState, D> = this.get().value.let{parse(it)}
    .mapLeft {
        require(this.instanceParameter != null)
        { "the function receiver type must be a Data class property, declared in the primary constructor"}
        val s = (this as CallableReference).boundReceiver as UiState
        s.toFocusAtView(this){ deliver(it.toMessage()) } /*as U*/ }


private fun <E, U: UiState> U.toFocusAtView(
    toFocus: KProperty0<Focusable<E>>,
    deliverMessage: () -> Unit = {}
): U {
    require(toFocus.name in this::class.primaryConstructor!!.parameters.map { it.name })
    { "property toFocus must be a Data class property declared in the primary constructor" }
    deliverMessage()
    val isFocused = toFocus.get().setFocus()
    return this.copyDataObject(toFocus to isFocused)
}

fun <U: UiState> U.clearFocus(): U {
    val dataClass = this::class
    require(dataClass.isData) { "Type of object to clear focus must be a data class" }
    val properties = dataClass.constructorProperties
        .filter { it.returnType.isSubtypeOf(ValidOrFocusedAtCheck::class.createType()) }
    val propertiesWithNewValues = properties.map{ it to
        it::class.members.first{ it.name == "clearFocus" }.call() }.toTypedArray()
    return this.copyDataObject(*propertiesWithNewValues)
}

