package com.masatomo.jiaolong.core.common

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import com.masatomo.jiaolong.core.validation.toValidator
import com.masatomo.jiaolong.core.validation.validateBy
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


interface LongValue : InlineDomainValue<Long> {

    companion object {
        operator fun invoke(value: Long) = Anonymous(value)

        @JvmInline
        value class Anonymous(override val value: Long) : LongValue
    }

    override fun isValid() = validateBy(*LongValidations.predefinedValidators)
}


inline fun <V : LongValue, reified R : LongValue> V.to(): R = R::class.primaryConstructor!!.call(value)


object LongValidations {
    val predefinedValidators by lazy {
        arrayOf(
            Positive::class.toValidator(Positive::validate),
            Negative::class.toValidator(Negative::validate),
            Minimum::class.toValidator(Minimum::validate),
            Maximum::class.toValidator(Maximum::validate),
            Even::class.toValidator(Even::validate),
            NotOddLong::class.toValidator(NotOddLong::validate),
        )
    }

    @Target(AnnotationTarget.CLASS)
    annotation class Positive

    @Target(AnnotationTarget.CLASS)
    annotation class Negative

    @Target(AnnotationTarget.CLASS)
    annotation class Minimum(
        val minimum: Long = Long.MIN_VALUE,
        val include: Boolean = true,
    )

    @Target(AnnotationTarget.CLASS)
    annotation class Maximum(
        val maximum: Long = Long.MAX_VALUE,
        val include: Boolean = true,
    )

    @Target(AnnotationTarget.CLASS)
    annotation class Even

    @Target(AnnotationTarget.CLASS)
    annotation class NotOddLong
}


private fun <V : LongValue> LongValidations.Positive.validate(target: V): NotPositiveLong<V>? =
    target.takeIf { it.value <= 0 }
        ?.let { NotPositiveLong(it::class, it.value) }

data class NotPositiveLong<V : LongValue>(
    override val kClass: KClass<out V>,
    val actual: Long,
) : InvalidDomainValue


private fun <V : LongValue> LongValidations.Negative.validate(target: V): NotNegativeLong<V>? =
    target.takeIf { it.value <= 0 }
        ?.let { NotNegativeLong(it::class, it.value) }

data class NotNegativeLong<V : LongValue>(
    override val kClass: KClass<out V>,
    val actual: Long,
) : InvalidDomainValue


private fun <V : LongValue> LongValidations.Minimum.validate(target: V): LessMinimumLong<V>? =
    target.takeIf { it.value < minimum || (!include && it.value == minimum) }
        ?.let { LessMinimumLong(it::class, it.value, minimum, include) }

data class LessMinimumLong<V : LongValue>(
    override val kClass: KClass<out V>,
    val actual: Long,
    val minimum: Long,
    val include: Boolean
) : InvalidDomainValue


private fun <V : LongValue> LongValidations.Maximum.validate(target: V): MoreMaximumLong<V>? =
    target.takeIf { maximum < it.value || (!include && it.value == maximum) }
        ?.let { MoreMaximumLong(it::class, it.value, maximum, include) }

data class MoreMaximumLong<V : LongValue>(
    override val kClass: KClass<out V>,
    val actual: Long,
    val minimum: Long,
    val include: Boolean
) : InvalidDomainValue


private fun <V : LongValue> LongValidations.Even.validate(target: V): NotEvenLong<V>? =
    target.takeIf { it.value % 2 == 1L }
        ?.let { NotEvenLong(it::class, it.value) }

data class NotEvenLong<V : LongValue>(
    override val kClass: KClass<out V>,
    val actual: Long,
) : InvalidDomainValue


private fun <V : LongValue> LongValidations.NotOddLong.validate(target: V): Invalid<V>? =
    target.takeIf { it.value % 2 == 0L }
        ?.let { Invalid(it::class, it.value) }

data class Invalid<V : LongValue>(
    override val kClass: KClass<out V>,
    val actual: Long,
) : InvalidDomainValue
