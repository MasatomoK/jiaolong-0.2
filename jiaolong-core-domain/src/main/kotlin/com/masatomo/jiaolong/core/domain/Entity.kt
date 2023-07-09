package com.masatomo.jiaolong.core.domain

import com.masatomo.jiaolong.core.domain.values.Id
import com.masatomo.jiaolong.core.domain.values.IntValue
import com.masatomo.jiaolong.core.domain.values.StringValue
import java.io.Serializable
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.full.memberProperties
import kotlin.reflect.full.primaryConstructor


annotation class EntityBuilder

interface DomainEntity<E : DomainEntity<E, I>, I : Id<E>> : Serializable {

    val id: I
    fun assigned(id: I): E

    // Cannot override equals in Kotlin...
    // https://stackoverflow.com/questions/53771394/override-and-implement-fn-from-class-in-interface
    // https://discuss.kotlinlang.org/t/interface-overrides-equals/8901
    // fun equals(other: Any?): Boolean = this === other || (this::class == other::class && this.id == other.id)

    // Cannot override toString in Kotlin...
    fun reflectionToString(): String = this.javaClass.kotlin.memberProperties
        .joinToString(",") { "${it.name}=${it.get(this).toString()}" }
        .let { "${this::class.simpleName}(${it})" }
}

class DomainEntityBuilderSupport<T : DomainEntity<*, *>> {

    val convertingMessages = mutableListOf<CannotConvert<*, *>>()

    fun build(supplier: () -> T): Result<T> = TODO()

    inline fun <reified T : IntValue> toIntValue(from: Int, property: KMutableProperty0<T?>): T =
        T::class.primaryConstructor!!.call(from).apply { property.set(this) }

    inline fun <reified T : IntValue> toIntValue(from: String, property: KMutableProperty0<T?>) =
        from.toIntOrNull()?.apply { toIntValue(this, property) } ?: convertingMessages.add(CannotConvert(from, Int::class))

    inline fun <reified T : StringValue> toStringValue(from: String, property: KMutableProperty0<T?>): T =
        T::class.primaryConstructor!!.call(from).apply { property.set(this) }

    inline fun <reified T : StringValue> toStringValue(from: Int, property: KMutableProperty0<T?>): T = toStringValue(from.toString(), property)
}


data class CannotConvert<T, U : Any>(val from: T, val to: KClass<U>)
