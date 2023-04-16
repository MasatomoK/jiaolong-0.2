package com.masatomo.jiaolong.core.domain.values

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import com.masatomo.jiaolong.core.validation.toValidator
import com.masatomo.jiaolong.core.validation.validateBy
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


interface LocalDateValue : InlineDomainValue<LocalDate> {

    companion object {
        operator fun invoke(value: LocalDate) = Anonymous(value)

        @JvmInline
        value class Anonymous(override val value: LocalDate) : LocalDateValue
    }

    override fun isValid() = validateBy(*LocalDateValidations.predefinedValidators)
}

inline fun <V : LocalDateValue, reified R : LocalDateValue> V.to(): R = R::class.primaryConstructor!!.call(value)


object LocalDateValidations {

    val predefinedValidators by lazy {
        arrayOf(
            Minimum::class.toValidator(Minimum::validate),
            Maximum::class.toValidator(Maximum::validate),
            DayOfWeek::class.toValidator(DayOfWeek::validate),
        )
    }

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

private fun <V : LocalDateValue> LocalDateValidations.Minimum.validate(target: V): LessLocalDate<V>? {
    val minimum = parse(minimumAsString, pattern)
    return target.takeIf { target.value < minimum }
        ?.let { LessLocalDate(it::class, it.value, minimum) }
}

data class LessLocalDate<V : LocalDateValue>(
    override val kClass: KClass<out V>,
    val actual: LocalDate,
    val limit: LocalDate
) : InvalidDomainValue


private fun <V : LocalDateValue> LocalDateValidations.Maximum.validate(target: V): MoreLocalDate<V>? {
    val maximum = parse(maximumAsString, pattern)
    return target.takeIf { maximum < target.value }
        ?.let { MoreLocalDate(it::class, it.value, maximum) }
}

data class MoreLocalDate<V : LocalDateValue>(
    override val kClass: KClass<out V>,
    val actual: LocalDate,
    val limit: LocalDate
) : InvalidDomainValue


private fun <V : LocalDateValue> LocalDateValidations.DayOfWeek.validate(target: V): OuterDayOfWeek<V>? =
    target.takeIf { !days.contains(target.value.dayOfWeek) }
        ?.let { OuterDayOfWeek(it::class, it.value, days.toSet()) }

data class OuterDayOfWeek<V : LocalDateValue>(
    override val kClass: KClass<out V>,
    val actual: LocalDate,
    val targets: Set<DayOfWeek>
) : InvalidDomainValue


private val formatCache = mutableMapOf<String, DateTimeFormatter>()
private val dateCache = mutableMapOf<String, LocalDate>()

private fun parse(from: String, pattern: String): LocalDate = dateCache.computeIfAbsent(from) {
    formatCache.computeIfAbsent(pattern) { DateTimeFormatter.ofPattern(pattern) }
        .let { LocalDate.parse(from, it) }
}
