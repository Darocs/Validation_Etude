package ru.polescanner.validation_etude.ui.reusable.kotlinapi

import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.instanceParameter
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.primaryConstructor

/**
 * A function to read a property from an instance of a class given the property name
 * throws exception if property not found
 * ### Example:
 * ```
 * readInstanceProperty<Int>(sample, "age")
 * ```
 * requires: kotlin reflection
 * @throws exception if property not found
 * @see [StackOverflow](https://stackoverflow.com/a/35539628/11600358)
 */
@Suppress("UNCHECKED_CAST")
fun <R> readInstanceProperty(instance: Any, propertyName: String): R {
    val property = instance::class.members
        // don't cast here to <Any, R>, it would succeed silently
        .first { it.name == propertyName } as KProperty1<Any, *>
    // force a invalid cast exception if incorrect type here
    return property.get(instance) as R
}

/**
 * The general invoke of [copy()](https://kotlinlang.org/docs/data-classes.html#copying)
 * of data class passing specific property values like you can do when calling copy directly:
 * ### Example:
 * ```
 * val person = Person("Jane", 23, Sex.FEMALE)
 * val copy = copyDataObject(person,
 *      person::name to "Jack",
 *      person::sex to Sex.MALE
 * )
 * ```
 *
 * @param [T] generic of the data
 * @param [properties] pairs of [T] property and new value to it
 * @return new instance of the same data class [T]
 * @see [StackOverflow](https://stackoverflow.com/a/77579481/11600358)
 *
 */
fun <T : Any> T.copyDataObject(vararg properties: Pair<KProperty<*>, Any?>): T {
    val dataClass = this::class
    require(dataClass.isData) { "Type of object to copy must be a data class" }
    val copyFunction = dataClass.memberFunctions.first { it.name == "copy" }
    val parameters = buildMap {
        put(copyFunction.instanceParameter!!, this@copyDataObject)
        properties.forEach { (property, value) ->
            val parameter = requireNotNull(
                copyFunction.parameters.firstOrNull { it.name == property.name }
            ) { "Parameter not found for property ${property.name}" }
            value?.let {
                require(
                    parameter.type.classifier == it::class //Not supported parent in parameter
                ) { "Incompatible type of value for property ${property.name}" }
            }
            put(parameter, value)
        }
    }
    @Suppress("UNCHECKED_CAST")
    return copyFunction.callBy(parameters) as T
}

val <T : Any> KClass<T>.constructorProperties
    get() =
        primaryConstructor?.let { ctor ->
            declaredMemberProperties.filter { prop ->
                ctor.parameters.any { param ->
                    param.name == prop.name
                            &&
                            param.type == prop.returnType
                }
            }
        } ?: emptyList()