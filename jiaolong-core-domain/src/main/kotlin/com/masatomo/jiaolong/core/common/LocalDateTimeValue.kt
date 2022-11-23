package com.masatomo.jiaolong.core.common

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import com.masatomo.jiaolong.core.validation.toValidator
import com.masatomo.jiaolong.core.validation.validateBy
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


interface LocalDateTimeValue : InlineDomainValue<LocalDateTime> {

    companion object {
        operator fun invoke(value: LocalDateTime) = Anonymous(value)

        @JvmInline
        value class Anonymous(override val value: LocalDateTime) : LocalDateTimeValue
    }

    override fun isValid() = validateBy(*LocalDateTimeValidations.predefinedValidators)
}

inline fun <V : LocalDateTimeValue, reified R : LocalDateTimeValue> V.to(): R = R::class.primaryConstructor!!.call(value)


object LocalDateTimeValidations {

    val predefinedValidators = arrayOf(
        Minimum::class.toValidator(Minimum::validate),
        Maximum::class.toValidator(Maximum::validate),
        DayOfWeek::class.toValidator(DayOfWeek::validate),
    )

    @Target(AnnotationTarget.CLASS)
    annotation class Minimum(
        val minimumAsString: String,
        val pattern: String = "uuuuMMdd"
    )

    @Target(AnnotationTarget.CLASS)
    annotation class Maximum(
        val maximumAsString: String,
        val pattern: String = "uuuuMMdd"
    )

    @Target(AnnotationTarget.CLASS)
    annotation class DayOfWeek(
        val days: Array<java.time.DayOfWeek>
    )
}


private fun <V : LocalDateTimeValue> LocalDateTimeValidations.Minimum.validate(target: V): LessLocalDateTime<V>? {
    val minimum = parse(minimumAsString, pattern)
    return target.takeIf { target.value < minimum }
        ?.let { LessLocalDateTime(it::class, it.value, minimum) }
}

data class LessLocalDateTime<V : LocalDateTimeValue>(
    override val kClass: KClass<out V>,
    val actual: LocalDateTime,
    val limit: LocalDateTime
) : InvalidDomainValue


private fun <V : LocalDateTimeValue> LocalDateTimeValidations.Maximum.validate(target: V): MoreLocalDateTime<V>? {
    val maximum = parse(maximumAsString, pattern)
    return target.takeIf { maximum < target.value }
        ?.let { MoreLocalDateTime(it::class, it.value, maximum) }
}

data class MoreLocalDateTime<V : LocalDateTimeValue>(
    override val kClass: KClass<out V>,
    val actual: LocalDateTime,
    val limit: LocalDateTime
) : InvalidDomainValue


private fun <V : LocalDateTimeValue> LocalDateTimeValidations.DayOfWeek.validate(target: V): OuterDayOfWeekLocalDateTime<V>? =
    target.takeIf { !days.contains(target.value.dayOfWeek) }
        ?.let { OuterDayOfWeekLocalDateTime(it::class, it.value, days.toSet()) }

data class OuterDayOfWeekLocalDateTime<V : LocalDateTimeValue>(
    override val kClass: KClass<out V>,
    val actual: LocalDateTime,
    val targets: Set<DayOfWeek>
) : InvalidDomainValue


private val formatCache = mutableMapOf<String, DateTimeFormatter>()
private val dateCache = mutableMapOf<String, LocalDateTime>()

private fun parse(from: String, pattern: String): LocalDateTime = dateCache.computeIfAbsent(from) {
    formatCache.computeIfAbsent(pattern) { DateTimeFormatter.ofPattern(pattern) }
        .let { LocalDateTime.parse(from, it) }
}
