package com.masatomo.jiaolong.core.validation.values

import com.masatomo.jiaolong.core.domain.values.DoubleValue
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import com.masatomo.jiaolong.core.validation.ValidationContext
import kotlin.reflect.KClass


fun <V : DoubleValue> ValidationContext<V>.positive() = target.takeIf {
    it.value <= 0
}?.apply { addReason(NotPositiveDouble(this::class, value)) }

data class NotPositiveDouble<V : DoubleValue>(
    override val kClass: KClass<out V>,
    val actual: Double,
) : InvalidDomainValue


fun <V : DoubleValue> ValidationContext<V>.negative() = target.takeIf {
    it.value >= 0
}?.apply { addReason(NotNegativeDouble(this::class, value)) }

data class NotNegativeDouble<V : DoubleValue>(
    override val kClass: KClass<out V>,
    val actual: Double,
) : InvalidDomainValue


fun <V : DoubleValue> ValidationContext<V>.minimumWith(
    minimum: Double = Double.MIN_VALUE,
    include: Boolean = true,
) = target.takeIf {
    it.value < minimum || (!include && it.value == minimum)
}?.apply { addReason(LessMinimumDouble(this::class, value, minimum, include)) }

data class LessMinimumDouble<V : DoubleValue>(
    override val kClass: KClass<out V>,
    val actual: Double,
    val minimum: Double,
    val include: Boolean
) : InvalidDomainValue


fun <V : DoubleValue> ValidationContext<V>.maximumWith(
    maximum: Double = Double.MAX_VALUE,
    include: Boolean = true,
) = target.takeIf {
    maximum < it.value || (!include && it.value == maximum)
}?.apply { addReason(MoreMaximumDouble(this::class, value, maximum, include)) }

data class MoreMaximumDouble<V : DoubleValue>(
    override val kClass: KClass<out V>,
    val actual: Double,
    val minimum: Double,
    val include: Boolean
) : InvalidDomainValue
