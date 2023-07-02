package com.masatomo.jiaolong.core.validation.values

import com.masatomo.jiaolong.core.domain.values.BigDecimalValue
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import com.masatomo.jiaolong.core.validation.ValidationContext
import java.math.BigDecimal
import kotlin.reflect.KClass


fun <V : BigDecimalValue> ValidationContext<V>.positive() = target.takeIf {
    it.value.signum() != 1
}?.apply { addReason(NotPositiveBigDecimal(this::class, value)) }

data class NotPositiveBigDecimal<V : BigDecimalValue>(
    override val kClass: KClass<out V>,
    val actual: BigDecimal,
) : InvalidDomainValue


fun <V : BigDecimalValue> ValidationContext<V>.negative() = target.takeIf {
    it.value.signum() != -1
}?.apply { addReason(NotNegativeBigDecimal(this::class, value)) }

data class NotNegativeBigDecimal<V : BigDecimalValue>(
    override val kClass: KClass<out V>,
    val actual: BigDecimal,
) : InvalidDomainValue


fun <V : BigDecimalValue> ValidationContext<V>.minimumWith(
    minimum: BigDecimal,
    include: Boolean = true,
) = target.takeIf {
    it.value < minimum || (!include && it.value == minimum)
}?.apply { addReason(LessMinimumBigDecimal(this::class, value, minimum, include)) }

data class LessMinimumBigDecimal<V : BigDecimalValue>(
    override val kClass: KClass<out V>,
    val actual: BigDecimal,
    val minimum: BigDecimal,
    val include: Boolean
) : InvalidDomainValue


fun <V : BigDecimalValue> ValidationContext<V>.maximumWith(
    maximum: BigDecimal,
    include: Boolean = true,
) = target.takeIf {
    maximum < it.value || (!include && it.value == maximum)
}?.apply { addReason(MoreMaximumBigDecimal(this::class, value, maximum, include)) }

data class MoreMaximumBigDecimal<V : BigDecimalValue>(
    override val kClass: KClass<out V>,
    val actual: BigDecimal,
    val minimum: BigDecimal,
    val include: Boolean
) : InvalidDomainValue
