package com.masatomo.jiaolong.core.common

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import com.masatomo.jiaolong.core.validation.AnnotatedValueValidator
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


interface LocalDateTimeValue<V : LocalDateTimeValue<V>> : InlineDomainValue<LocalDateTime, V> {

    companion object {
        operator fun invoke(value: LocalDateTime) = Anonymous(value)
    }


    @JvmInline
    value class Anonymous(override val value: LocalDateTime) : LocalDateTimeValue<Anonymous> {
        override fun isValid(): Collection<InvalidDomainValue<Anonymous>> = emptyList()
    }

    override val annotatedValidators: Array<AnnotatedValueValidator<V>>
        get() = LocalDateTimeValidations.allValidator()
}

inline fun <V : LocalDateTimeValue<V>, reified R : LocalDateTimeValue<R>> V.to(): R = R::class.primaryConstructor!!.call(value)


object LocalDateTimeValidations {

    @Suppress("UNCHECKED_CAST")
    fun <V : LocalDateTimeValue<V>> allValidator(): Array<AnnotatedValueValidator<V>> = arrayOf(
        Minimum::class to { (this as Minimum).validate(it as V) },
        Maximum::class to { (this as Maximum).validate(it as V) },
        DayOfWeek::class to { (this as DayOfWeek).validate(it as V) },
    )


    private val formatCache = mutableMapOf<String, DateTimeFormatter>()
    private val dateCache = mutableMapOf<String, LocalDateTime>()

    private fun parse(from: String, pattern: String): LocalDateTime = dateCache.computeIfAbsent(from) {
        formatCache.computeIfAbsent(pattern) { DateTimeFormatter.ofPattern(pattern) }
            .let { LocalDateTime.parse(from, it) }
    }

    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Minimum(
        val minimumAsString: String,
        val pattern: String = "uuuuMMdd"
    ) {
        data class LessLocalDateTime<V : LocalDateTimeValue<V>>(
            override val kClass: KClass<out V>,
            val actual: LocalDateTime,
            val limit: LocalDateTime
        ) : InvalidDomainValue<V>
    }

    private fun <V : LocalDateTimeValue<V>> Minimum.validate(target: V): Minimum.LessLocalDateTime<V>? {
        val minimum = parse(minimumAsString, pattern)
        return target.takeIf { target.value < minimum }
            ?.let { Minimum.LessLocalDateTime(it::class, it.value, minimum) }
    }


    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class Maximum(
        val maximumAsString: String,
        val pattern: String = "uuuuMMdd"
    ) {
        data class MoreLocalDateTime<V : LocalDateTimeValue<V>>(
            override val kClass: KClass<out V>,
            val actual: LocalDateTime,
            val limit: LocalDateTime
        ) : InvalidDomainValue<V>
    }

    private fun <V : LocalDateTimeValue<V>> Maximum.validate(target: V): Maximum.MoreLocalDateTime<V>? {
        val maximum = parse(maximumAsString, pattern)
        return target.takeIf { maximum < target.value }
            ?.let { Maximum.MoreLocalDateTime(it::class, it.value, maximum) }
    }


    @Target(AnnotationTarget.CLASS)
    @Retention(AnnotationRetention.RUNTIME)
    annotation class DayOfWeek(
        val days: Array<java.time.DayOfWeek>
    ) {
        data class OuterDayOfWeekLocalDateTime<V : LocalDateTimeValue<V>>(
            override val kClass: KClass<out V>,
            val actual: LocalDateTime,
            val targets: Set<java.time.DayOfWeek>
        ) : InvalidDomainValue<V>
    }

    private fun <V : LocalDateTimeValue<V>> DayOfWeek.validate(target: V): DayOfWeek.OuterDayOfWeekLocalDateTime<V>? =
        target.takeIf { !days.contains(target.value.dayOfWeek) }
            ?.let { DayOfWeek.OuterDayOfWeekLocalDateTime(it::class, it.value, days.toSet()) }
}
