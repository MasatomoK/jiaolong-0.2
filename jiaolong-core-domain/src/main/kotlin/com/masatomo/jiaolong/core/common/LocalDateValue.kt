package com.masatomo.jiaolong.core.common

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import com.masatomo.jiaolong.core.validation.AnnotatedValueValidator
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


interface LocalDateValue<V : LocalDateValue<V>> : InlineDomainValue<LocalDate, V> {

    companion object {
        operator fun invoke(value: LocalDate) = Anonymous(value)
    }

    @JvmInline
    value class Anonymous(override val value: LocalDate) : LocalDateValue<Anonymous> {
        override fun isValid(): Collection<InvalidDomainValue<Anonymous>> = emptyList()
    }

    override val annotatedValidators: Array<AnnotatedValueValidator<V>>
        get() = LocalDateValidations.allValidator()
}

inline fun <V : LocalDateValue<V>, reified R : LocalDateValue<R>> V.to(): R = R::class.primaryConstructor!!.call(value)


object LocalDateValidations {

    @Suppress("UNCHECKED_CAST")
    fun <V : LocalDateValue<V>> allValidator(): Array<AnnotatedValueValidator<V>> = arrayOf(
        Minimum::class to { (this as Minimum).validate(it as V) },
        Maximum::class to { (this as Maximum).validate(it as V) },
        DayOfWeek::class to { (this as DayOfWeek).validate(it as V) },
    )


    private val formatCache = mutableMapOf<String, DateTimeFormatter>()
    private val dateCache = mutableMapOf<String, LocalDate>()

    private fun parse(from: String, pattern: String): LocalDate = dateCache.computeIfAbsent(from) {
        formatCache.computeIfAbsent(pattern) { DateTimeFormatter.ofPattern(pattern) }
            .let { LocalDate.parse(from, it) }
    }

    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Minimum(
        val minimumAsString: String,
        val pattern: String = "uuuuMMdd"
    )

    private fun <V : LocalDateValue<V>> Minimum.validate(target: V): LessLocalDate<V>? {
        val minimum = parse(minimumAsString, pattern)
        return target.takeIf { target.value < minimum }
            ?.let { LessLocalDate(it::class, it.value, minimum) }
    }


    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Maximum(
        val maximumAsString: String,
        val pattern: String = "uuuuMMdd"
    )

    private fun <V : LocalDateValue<V>> Maximum.validate(target: V): MoreLocalDate<V>? {
        val maximum = parse(maximumAsString, pattern)
        return target.takeIf { maximum < target.value }
            ?.let { MoreLocalDate(it::class, it.value, maximum) }
    }


    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class DayOfWeek(
        val days: Array<java.time.DayOfWeek>
    )

    private fun <V : LocalDateValue<V>> DayOfWeek.validate(target: V): OuterDayOfWeek<V>? =
        target.takeIf { !days.contains(target.value.dayOfWeek) }
            ?.let { OuterDayOfWeek(it::class, it.value, days.toSet()) }

    data class LessLocalDate<V : LocalDateValue<V>>(
        override val kClass: KClass<out V>,
        val actual: LocalDate,
        val limit: LocalDate
    ) : InvalidDomainValue<V>

    data class MoreLocalDate<V : LocalDateValue<V>>(
        override val kClass: KClass<out V>,
        val actual: LocalDate,
        val limit: LocalDate
    ) : InvalidDomainValue<V>

    data class OuterDayOfWeek<V : LocalDateValue<V>>(
        override val kClass: KClass<out V>,
        val actual: LocalDate,
        val targets: Set<java.time.DayOfWeek>
    ) : InvalidDomainValue<V>
}
