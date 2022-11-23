package com.masatomo.jiaolong.core.common

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import com.masatomo.jiaolong.core.validation.toValidator
import com.masatomo.jiaolong.core.validation.validateBy
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


interface DoubleValue : InlineDomainValue<Double> {

    companion object {
        operator fun invoke(value: Double) = Anonymous(value)

        @JvmInline
        value class Anonymous(override val value: Double) : DoubleValue
    }

    override fun isValid() = validateBy(*DoubleValidations.predefinedValidators)
}


inline fun <V : DoubleValue, reified R : DoubleValue> V.to(): R = R::class.primaryConstructor!!.call(value)


object DoubleValidations {

    val predefinedValidators by lazy {
        arrayOf(
            Positive::class.toValidator(Positive::validate),
            Negative::class.toValidator(Negative::validate),
            Minimum::class.toValidator(Minimum::validate),
            Maximum::class.toValidator(Maximum::validate),
        )
    }

    @Target(AnnotationTarget.CLASS)
    annotation class Positive

    @Target(AnnotationTarget.CLASS)
    annotation class Negative

    @Target(AnnotationTarget.CLASS)
    annotation class Minimum(
        val minimum: Double = Double.MIN_VALUE,
        val include: Boolean = true,
    )

    @Target(AnnotationTarget.CLASS)
    annotation class Maximum(
        val maximum: Double = Double.MAX_VALUE,
        val include: Boolean = true,
    )
}


private fun <V : DoubleValue> DoubleValidations.Positive.validate(target: V): NotPositiveDouble<V>? =
    target.takeIf { it.value <= 0 }
        ?.let { NotPositiveDouble(it::class, it.value) }

data class NotPositiveDouble<V : DoubleValue>(
    override val kClass: KClass<out V>,
    val actual: Double,
) : InvalidDomainValue


private fun <V : DoubleValue> DoubleValidations.Negative.validate(target: V): NotNegativeDouble<V>? =
    target.takeIf { it.value <= 0 }
        ?.let { NotNegativeDouble(it::class, it.value) }

data class NotNegativeDouble<V : DoubleValue>(
    override val kClass: KClass<out V>,
    val actual: Double,
) : InvalidDomainValue


private fun <V : DoubleValue> DoubleValidations.Minimum.validate(target: V): LessDouble<V>? =
    target.takeIf { it.value < minimum || (!include && it.value == minimum) }
        ?.let { LessDouble(it::class, it.value, minimum, include) }

data class LessDouble<V : DoubleValue>(
    override val kClass: KClass<out V>,
    val actual: Double,
    val minimum: Double,
    val include: Boolean
) : InvalidDomainValue


private fun <V : DoubleValue> DoubleValidations.Maximum.validate(target: V): MoreDouble<V>? =
    target.takeIf { maximum < it.value || (!include && it.value == maximum) }
        ?.let { MoreDouble(it::class, it.value, maximum, include) }

data class MoreDouble<V : DoubleValue>(
    override val kClass: KClass<out V>,
    val actual: Double,
    val minimum: Double,
    val include: Boolean
) : InvalidDomainValue
