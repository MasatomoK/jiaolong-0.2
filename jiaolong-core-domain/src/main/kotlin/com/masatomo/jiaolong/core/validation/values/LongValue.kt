package com.masatomo.jiaolong.core.validation.values

import com.masatomo.jiaolong.core.domain.values.LongValue
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import com.masatomo.jiaolong.core.validation.ValidationContext
import kotlin.reflect.KClass


fun <V : LongValue> ValidationContext<V>.positive() = target.takeIf {
    it.value <= 0
}?.apply { addReason(NotPositiveLong(this::class, value)) }

data class NotPositiveLong<V : LongValue>(
    override val kClass: KClass<out V>,
    val actual: Long,
) : InvalidDomainValue


fun <V : LongValue> ValidationContext<V>.negative() = target.takeIf {
    it.value >= 0
}?.apply { addReason(NotNegativeLong(this::class, value)) }

data class NotNegativeLong<V : LongValue>(
    override val kClass: KClass<out V>,
    val actual: Long,
) : InvalidDomainValue


fun <V : LongValue> ValidationContext<V>.minimumWith(
    minimum: Long = Long.MIN_VALUE,
    include: Boolean = true,
) = target.takeIf {
    it.value < minimum || (!include && it.value == minimum)
}?.apply { addReason(LessMinimumLong(this::class, value, minimum, include)) }

data class LessMinimumLong<V : LongValue>(
    override val kClass: KClass<out V>,
    val actual: Long,
    val minimum: Long,
    val include: Boolean
) : InvalidDomainValue


fun <V : LongValue> ValidationContext<V>.maximumWith(
    maximum: Long = Long.MAX_VALUE,
    include: Boolean = true,
) = target.takeIf {
    maximum < it.value || (!include && it.value == maximum)
}?.apply { addReason(MoreMaximumLong(this::class, value, maximum, include)) }

data class MoreMaximumLong<V : LongValue>(
    override val kClass: KClass<out V>,
    val actual: Long,
    val minimum: Long,
    val include: Boolean
) : InvalidDomainValue


fun <V : LongValue> ValidationContext<V>.even() = target.takeIf {
    it.value % 2 != 0L
}?.apply { addReason(NotEvenLong(this::class, value)) }

data class NotEvenLong<V : LongValue>(
    override val kClass: KClass<out V>,
    val actual: Long,
) : InvalidDomainValue


fun <V : LongValue> ValidationContext<V>.odd() = target.takeIf {
    it.value % 2 == 0L
}?.apply { addReason(NotOddLong(this::class, value)) }

data class NotOddLong<V : LongValue>(
    override val kClass: KClass<out V>,
    val actual: Long,
) : InvalidDomainValue
