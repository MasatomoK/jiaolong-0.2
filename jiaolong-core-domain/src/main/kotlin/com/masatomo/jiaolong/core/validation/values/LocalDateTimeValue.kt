package com.masatomo.jiaolong.core.validation.values

import com.masatomo.jiaolong.core.domain.values.LocalDateTimeValue
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import com.masatomo.jiaolong.core.validation.ValidationContext
import java.time.DayOfWeek
import java.time.LocalDateTime
import kotlin.reflect.KClass


fun <V : LocalDateTimeValue> ValidationContext<V>.after(base: LocalDateTime) = target.takeIf {
    !target.value.isAfter(base)
}?.apply { addReason(NotAfterLocalDateTime(this::class, value, base)) }

data class NotAfterLocalDateTime<V : LocalDateTimeValue>(
    override val kClass: KClass<out V>, val actual: LocalDateTime, val limit: LocalDateTime
) : InvalidDomainValue


fun <V : LocalDateTimeValue> ValidationContext<V>.before(base: LocalDateTime) = target.takeIf {
    !target.value.isBefore(base)
}?.apply { addReason(NotBeforeLocalDateTime(this::class, value, base)) }

data class NotBeforeLocalDateTime<V : LocalDateTimeValue>(
    override val kClass: KClass<out V>, val actual: LocalDateTime, val limit: LocalDateTime
) : InvalidDomainValue


fun <V : LocalDateTimeValue> ValidationContext<V>.dayOfWeeksIn(vararg days: DayOfWeek) = target.takeIf {
    !days.contains(target.value.dayOfWeek)
}?.apply { addReason(OuterDayOfWeekLocalDateTime(this::class, value, days.toSet())) }

data class OuterDayOfWeekLocalDateTime<V : LocalDateTimeValue>(
    override val kClass: KClass<out V>, val actual: LocalDateTime, val targets: Set<DayOfWeek>
) : InvalidDomainValue
