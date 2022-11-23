package com.masatomo.jiaolong.core.domain

import com.masatomo.jiaolong.core.common.Id
import com.masatomo.jiaolong.core.common.IntValue
import com.masatomo.jiaolong.core.common.StringValue
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty0
import kotlin.reflect.full.primaryConstructor


interface DomainEntity<E : DomainEntity<E>> {

    val id: Id<E>
    fun isValid(): Collection<InvalidDomainEntity<*>> = emptyList()

    // Cannot override equals in Kotlin...
    // https://stackoverflow.com/questions/53771394/override-and-implement-fn-from-class-in-interface
    // https://discuss.kotlinlang.org/t/interface-overrides-equals/8901
    // fun equals(other: Any?): Boolean = this === other || (this::class == other::class && this.id == other.id)
}


interface InvalidDomainEntity<E : DomainEntity<E>> {
    val kClass: KClass<out E>
}


interface EntityBuilder<E : DomainEntity<E>> {
    fun build(): Result<E>
    data class BuildingException(val notConvertibleMessages: Collection<NotConvertibleMessage>) : JiaolongRuntimeException()
}


class DomainEntityBuilderSupport<T : DomainEntity<*>> {

    val convertingMessages = mutableListOf<NotConvertibleMessage>()

    fun build(supplier: () -> T): Result<T> =
        if (convertingMessages.isEmpty()) Result.success(supplier())
        else Result.failure(EntityBuilder.BuildingException(convertingMessages))

    inline fun <reified T : IntValue<*>> toIntValue(from: Int, property: KMutableProperty0<T?>): T =
        T::class.primaryConstructor!!.call(from).apply { property.set(this) }

    inline fun <reified T : IntValue<*>> toIntValue(from: String, property: KMutableProperty0<T?>) =
        from.toIntOrNull()?.apply { toIntValue(this, property) } ?: convertingMessages.add(CannotConvert(from, Int::class))

    inline fun <reified T : StringValue<*>> toStringValue(from: String, property: KMutableProperty0<T?>): T =
        T::class.primaryConstructor!!.call(from).apply { property.set(this) }

    inline fun <reified T : StringValue<*>> toStringValue(from: Int, property: KMutableProperty0<T?>): T = toStringValue(from.toString(), property)
}


interface NotConvertibleMessage
data class CannotConvert<T, U : Any>(val from: T, val to: KClass<U>) : NotConvertibleMessage
