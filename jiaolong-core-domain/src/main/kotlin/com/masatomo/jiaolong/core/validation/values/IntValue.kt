package com.masatomo.jiaolong.core.validation.values

import com.masatomo.jiaolong.core.domain.values.IntValue
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import com.masatomo.jiaolong.core.validation.ValidationContext
import kotlin.reflect.KClass


fun <V : IntValue> ValidationContext<V>.positive() = target.takeIf {
    it.value <= 0
}?.apply { addReason(NotPositiveInt(this::class, value)) }

data class NotPositiveInt<V : IntValue>(
    override val kClass: KClass<out V>,
    val actual: Int,
) : InvalidDomainValue


fun <V : IntValue> ValidationContext<V>.negative() = target.takeIf {
    it.value >= 0
}?.apply { addReason(NotNegativeInt(this::class, value)) }

data class NotNegativeInt<V : IntValue>(
    override val kClass: KClass<out V>,
    val actual: Int,
) : InvalidDomainValue


fun <V : IntValue> ValidationContext<V>.minimumWith(
    minimum: Int = Int.MIN_VALUE,
    include: Boolean = true,
) = target.takeIf {
    it.value < minimum || (!include && it.value == minimum)
}?.apply { addReason(LessMinimumInt(this::class, value, minimum, include)) }

data class LessMinimumInt<V : IntValue>(
    override val kClass: KClass<out V>,
    val actual: Int,
    val minimum: Int,
    val include: Boolean
) : InvalidDomainValue


fun <V : IntValue> ValidationContext<V>.maximumWith(
    maximum: Int = Int.MAX_VALUE,
    include: Boolean = true,
) = target.takeIf {
    maximum < it.value || (!include && it.value == maximum)
}?.apply { addReason(MoreMaximumInt(this::class, value, maximum, include)) }

data class MoreMaximumInt<V : IntValue>(
    override val kClass: KClass<out V>,
    val actual: Int,
    val minimum: Int,
    val include: Boolean
) : InvalidDomainValue


fun <V : IntValue> ValidationContext<V>.even() = target.takeIf {
    it.value % 2 != 0
}?.apply { addReason(NotEvenInt(this::class, value)) }

data class NotEvenInt<V : IntValue>(
    override val kClass: KClass<out V>,
    val actual: Int,
) : InvalidDomainValue


fun <V : IntValue> ValidationContext<V>.odd() = target.takeIf {
    it.value % 2 == 0
}?.apply { addReason(NotOddInt(this::class, value)) }

data class NotOddInt<V : IntValue>(
    override val kClass: KClass<out V>,
    val actual: Int,
) : InvalidDomainValue
