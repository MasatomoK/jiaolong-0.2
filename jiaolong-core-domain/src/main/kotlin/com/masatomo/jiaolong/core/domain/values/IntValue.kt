package com.masatomo.jiaolong.core.domain.values

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import com.masatomo.jiaolong.core.validation.toValidator
import com.masatomo.jiaolong.core.validation.validateBy
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


interface IntValue : InlineDomainValue<Int> {

    companion object {
        operator fun invoke(value: Int) = Anonymous(value)

        @JvmInline
        value class Anonymous(override val value: Int) : IntValue
    }

    override fun isValid(): Collection<InvalidDomainValue> = validateBy(*IntValidations.predefinedValidators)
}

object IntValidations {

    val predefinedValidators by lazy {
        arrayOf(
            Positive::class.toValidator(Positive::validate),
            Negative::class.toValidator(Negative::validate),
            Minimum::class.toValidator(Minimum::validate),
            Maximum::class.toValidator(Maximum::validate),
            Even::class.toValidator(Even::validate),
            Odd::class.toValidator(Odd::validate),
        )
    }

    @Target(AnnotationTarget.CLASS)
    annotation class Positive

    @Target(AnnotationTarget.CLASS)
    annotation class Negative

    @Target(AnnotationTarget.CLASS)
    annotation class Minimum(
        val minimum: Int = Int.MIN_VALUE,
        val include: Boolean = true,
    )

    @Target(AnnotationTarget.CLASS)
    @MustBeDocumented
    annotation class Maximum(
        val maximum: Int = Int.MAX_VALUE,
        val include: Boolean = true,
    )

    @Target(AnnotationTarget.CLASS)
    annotation class Even


    @Target(AnnotationTarget.CLASS)
    annotation class Odd
}


private fun <V : IntValue> IntValidations.Positive.validate(target: V): NotPositiveInt<V>? =
    target.takeIf { it.value <= 0 }
        ?.let { NotPositiveInt(it::class, it.value) }

data class NotPositiveInt<V : IntValue>(
    override val kClass: KClass<out V>,
    val actual: Int,
) : InvalidDomainValue


private fun <V : IntValue> IntValidations.Negative.validate(target: V): NotNegativeInt<V>? =
    target.takeIf { it.value >= 0 }
        ?.let { NotNegativeInt(it::class, it.value) }

data class NotNegativeInt<V : IntValue>(
    override val kClass: KClass<out V>,
    val actual: Int,
) : InvalidDomainValue


private fun <V : IntValue> IntValidations.Minimum.validate(target: V): LessMinimumInt<V>? =
    target.takeIf { it.value < minimum || (!include && it.value == minimum) }
        ?.let { LessMinimumInt(it::class, it.value, minimum, include) }

data class LessMinimumInt<V : IntValue>(
    override val kClass: KClass<out V>,
    val actual: Int,
    val minimum: Int,
    val include: Boolean
) : InvalidDomainValue


private fun <V : IntValue> IntValidations.Maximum.validate(target: V): MoreMaximumInt<V>? =
    target.takeIf { maximum < it.value || (!include && it.value == maximum) }
        ?.let { MoreMaximumInt(it::class, it.value, maximum, include) }

data class MoreMaximumInt<V : IntValue>(
    override val kClass: KClass<out V>,
    val actual: Int,
    val minimum: Int,
    val include: Boolean
) : InvalidDomainValue


private fun <V : IntValue> IntValidations.Even.validate(target: V): NotEvenInt<V>? =
    target.takeIf { it.value % 2 == 1 }
        ?.let { NotEvenInt(it::class, it.value) }

data class NotEvenInt<V : IntValue>(
    override val kClass: KClass<out V>,
    val actual: Int,
) : InvalidDomainValue


private fun <V : IntValue> IntValidations.Odd.validate(target: V): NotOddInt<V>? =
    target.takeIf { it.value % 2 == 0 }
        ?.let { NotOddInt(it::class, it.value) }

data class NotOddInt<V : IntValue>(
    override val kClass: KClass<out V>,
    val actual: Int,
) : InvalidDomainValue


inline fun <V : IntValue, reified R : IntValue> V.to(): R = R::class.primaryConstructor!!.call(value)
