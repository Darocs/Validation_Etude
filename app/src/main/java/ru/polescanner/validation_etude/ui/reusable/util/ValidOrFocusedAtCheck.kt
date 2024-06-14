package ru.polescanner.validation_etude.ui.reusable.util

import android.util.Log
import arrow.core.Either
import ru.polescanner.validation_etude.domain.general.ErrorType
import ru.polescanner.validation_etude.ui.reusable.kotlinapi.copyDataObject
import ru.polescanner.validation_etude.ui.reusable.kotlinapi.preparePropertiesOfType
import ru.polescanner.validation_etude.ui.reusable.kotlinapi.readInstanceProperty
import kotlin.jvm.internal.CallableReference
import kotlin.reflect.KClass
import kotlin.reflect.KProperty0
import kotlin.reflect.KProperty1
import kotlin.reflect.full.createType
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor

// ToDo split code to Reflection and normal code. My idea that this class can't depend on kotlin.reflect
private const val TAG = "ValidOrFocusedAtCheck"

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
 * @throws [IllegalArgumentException] if no properties of [ValidOrFocusedAtCheck] type
 */
fun <U: UiState> U.atMostOneFocused(): Boolean {
    val kClass = this::class
    require(kClass.isData) { "Only data classes instances are supported" }
    val propertyNames = kClass.memberProperties
        .filter{ it.returnType.classifier == ValidOrFocusedAtCheck::class }
        .map { it.name }
    require(propertyNames.size > 0){ "No Focusable properties in the receiver UiState"}
    val propertyValues = propertyNames.map {
        readInstanceProperty<ValidOrFocusedAtCheck<*>>(this, it)
    }
    return propertyValues.count { it.isFocused } <= 1
}

//https://stackoverflow.com/questions/43822920/kotlin-check-if-function-requires-instance-parameter
fun <E, D: Any> KProperty0<Focusable<E>>.parseOrPrompt(
    deliver: (UiText) -> Unit,
    parse: (E) -> Either<ErrorType, D>
): Either<UiState, D> = this.get().value.let{parse(it)}
    .mapLeft {
        /*Log.d(TAG, "parseOrPrompt: ${this@parseOrPrompt.instanceParameter}")
        require(this@parseOrPrompt.instanceParameter != null)
        { "the function receiver type must be a Data class property, declared in the primary constructor"}*/
        val s = (this as CallableReference).boundReceiver as UiState
        Log.d(TAG, "parseOrPrompt: UiState $s")
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
    val properties = dataClass.memberProperties
        .filter { it.returnType.classifier == ValidOrFocusedAtCheck::class }
    require(properties.size > 0){" UiState has no Focused properties " }
    Log.d(TAG, "properties size:${properties.size} and ${properties[0]}")
    val propertiesWithNewValues = properties.map{
        @Suppress("UNCHECKED_CAST")
        it to run {
            val funClearFocus = (it.returnType.classifier as KClass<ValidOrFocusedAtCheck<*>>)
                .memberFunctions.first { it.name == "clearFocus" }
            Log.d(TAG, "clearFocus before: ${funClearFocus} ")
            val focusedPropertyValue = it.getter.call(this)
            Log.d(TAG, "loginValue: ${focusedPropertyValue} ")
            val focusedPropertyValueWithClearedFocus = funClearFocus.call(focusedPropertyValue)
            Log.d(TAG, "clearFocus: ${funClearFocus} :: $focusedPropertyValueWithClearedFocus")
            focusedPropertyValueWithClearedFocus
        }
    }.toTypedArray()
    return this.copyDataObject(*propertiesWithNewValues)
}

fun <T, V> getValue(prop: KProperty1<T, V>, instance: T): V = prop.get(instance)

fun <U: UiState> U.clearFocus1(): U {
    val properties = this.preparePropertiesOfType(
        ValidOrFocusedAtCheck::class,
        "clearFocus")
    return this.copyDataObject(*properties)
}

inline fun <reified T: Any> T.printProperties() {
    T::class.memberProperties.forEach { property ->
        println("${property.name} = ${property.get(this)}") // You can't use `this.property` here
    }
}