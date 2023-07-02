package com.masatomo.jiaolong.core.validation.values

import com.masatomo.jiaolong.core.domain.values.LocalDateValue
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import com.masatomo.jiaolong.core.validation.ValidationContext
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.reflect.KClass


fun <V : LocalDateValue> ValidationContext<V>.after(base: LocalDate) = target.takeIf {
    !target.value.isAfter(base)
}?.apply { addReason(NotAfterLocalDate(this::class, value, base)) }

data class NotAfterLocalDate<V : LocalDateValue>(
    override val kClass: KClass<out V>, val actual: LocalDate, val limit: LocalDate
) : InvalidDomainValue


fun <V : LocalDateValue> ValidationContext<V>.before(base: LocalDate) = target.takeIf {
    !target.value.isBefore(base)
}?.apply { addReason(NotBeforeLocalDate(this::class, value, base)) }

data class NotBeforeLocalDate<V : LocalDateValue>(
    override val kClass: KClass<out V>, val actual: LocalDate, val limit: LocalDate
) : InvalidDomainValue


fun <V : LocalDateValue> ValidationContext<V>.dayOfWeeksIn(vararg days: DayOfWeek) = target.takeIf {
    !days.contains(target.value.dayOfWeek)
}?.apply { addReason(OuterDayOfWeek(this::class, value, days.toSet())) }

data class OuterDayOfWeek<V : LocalDateValue>(
    override val kClass: KClass<out V>, val actual: LocalDate, val targets: Set<DayOfWeek>
) : InvalidDomainValue
