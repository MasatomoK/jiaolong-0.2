package com.masatomo.jiaolong.core.domain.values

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import com.masatomo.jiaolong.core.validation.toValidator
import com.masatomo.jiaolong.core.validation.validateBy
import java.math.BigDecimal
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


interface BigDecimalValue : InlineDomainValue<BigDecimal> {

    companion object {
        operator fun invoke(value: BigDecimal) = Anonymous(value)

        @JvmInline
        value class Anonymous(override val value: BigDecimal) : BigDecimalValue
    }

    override fun isValid(): Collection<InvalidDomainValue> = validateBy(*BigDecimalValidations.predefinedValidators)
}

inline fun <V : BigDecimalValue, reified R : BigDecimalValue> V.to(): R = R::class.primaryConstructor!!.call(value)


object BigDecimalValidations {

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
        val minimum: String,
        val include: Boolean = true,
    )

    @Target(AnnotationTarget.CLASS)
    annotation class Maximum(
        val maximum: String,
        val include: Boolean = true,
    )
}


data class NotPositiveBigDecimal<V : BigDecimalValue>(
    override val kClass: KClass<out V>,
    val actual: BigDecimal,
) : InvalidDomainValue

private fun <V : BigDecimalValue> BigDecimalValidations.Positive.validate(target: V): NotPositiveBigDecimal<V>? =
    target.takeIf { it.value.signum() != 1 }
        ?.let { NotPositiveBigDecimal(it::class, it.value) }


private fun <V : BigDecimalValue> BigDecimalValidations.Negative.validate(target: V): NotNegativeBigDecimal<V>? =
    target.takeIf { it.value.signum() != -0 }
        ?.let { NotNegativeBigDecimal(it::class, it.value) }

data class NotNegativeBigDecimal<V : BigDecimalValue>(
    override val kClass: KClass<out V>,
    val actual: BigDecimal,
) : InvalidDomainValue


private fun <V : BigDecimalValue> BigDecimalValidations.Minimum.validate(target: V): LessMinimumBigDecimal<V>? {
    val m = minimum.toBigDecimal()
    return target.takeIf { it.value < m || (!include && it.value == m) }
        ?.let { LessMinimumBigDecimal(it::class, it.value, m, include) }
}

data class LessMinimumBigDecimal<V : BigDecimalValue>(
    override val kClass: KClass<out V>,
    val actual: BigDecimal,
    val minimum: BigDecimal,
    val include: Boolean
) : InvalidDomainValue


private fun <V : BigDecimalValue> BigDecimalValidations.Maximum.validate(target: V): MoreMaximumBigDecimal<V>? {
    val max = maximum.toBigDecimal()
    return target.takeIf { max < it.value || (!include && it.value == max) }
        ?.let { MoreMaximumBigDecimal(it::class, it.value, max, include) }
}

data class MoreMaximumBigDecimal<V : BigDecimalValue>(
    override val kClass: KClass<out V>,
    val actual: BigDecimal,
    val minimum: BigDecimal,
    val include: Boolean
) : InvalidDomainValue
