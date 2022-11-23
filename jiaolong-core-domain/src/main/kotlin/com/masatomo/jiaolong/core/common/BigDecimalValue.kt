package com.masatomo.jiaolong.core.common

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import com.masatomo.jiaolong.core.validation.AnnotatedValueValidator
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import java.math.BigDecimal
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


interface BigDecimalValue<V : BigDecimalValue<V>> : InlineDomainValue<BigDecimal, V> {

    companion object {
        operator fun invoke(value: BigDecimal) = Anonymous(value)
    }

    @JvmInline
    value class Anonymous(override val value: BigDecimal) : BigDecimalValue<Anonymous> {
        override fun isValid(): Collection<InvalidDomainValue<Anonymous>> = emptyList()
    }

    override val annotatedValidators: Array<AnnotatedValueValidator<V>>
        get() = Validations.allValidator()

    object Validations {

        @Suppress("UNCHECKED_CAST")
        fun <V : BigDecimalValue<V>> allValidator(): Array<AnnotatedValueValidator<V>> = arrayOf(
            Positive::class to { (this as Positive).validate(it as V) },
            Negative::class to { (this as Negative).validate(it as V) },
            Minimum::class to { (this as Minimum).validate(it as V) },
            Maximum::class to { (this as Maximum).validate(it as V) },
        )


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Positive {

            data class NotPositiveBigDecimal<T : BigDecimalValue<T>>(
                override val kClass: KClass<out T>,
                val actual: BigDecimal,
            ) : InvalidDomainValue<T>
        }

        private fun <V : BigDecimalValue<V>> Positive.validate(target: V): Positive.NotPositiveBigDecimal<V>? =
            target.takeIf { it.value.signum() != 1 }
                ?.let { Positive.NotPositiveBigDecimal(it::class, it.value) }


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Negative {
            data class NotNegativeBigDecimal<T : BigDecimalValue<T>>(
                override val kClass: KClass<out T>,
                val actual: BigDecimal,
            ) : InvalidDomainValue<T>
        }

        private fun <V : BigDecimalValue<V>> Negative.validate(target: V): Negative.NotNegativeBigDecimal<V>? =
            target.takeIf { it.value.signum() != -0 }
                ?.let { Negative.NotNegativeBigDecimal(it::class, it.value) }


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Minimum(
            val minimum: String,
            val include: Boolean = true,
        ) {
            data class LessMinimumBigDecimal<T : BigDecimalValue<T>>(
                override val kClass: KClass<out T>,
                val actual: BigDecimal,
                val minimum: BigDecimal,
                val include: Boolean
            ) : InvalidDomainValue<T>
        }

        private fun <V : BigDecimalValue<V>> Minimum.validate(target: V): Minimum.LessMinimumBigDecimal<V>? {
            val m = minimum.toBigDecimal()
            return target.takeIf { it.value < m || (!include && it.value == m) }
                ?.let { Minimum.LessMinimumBigDecimal(it::class, it.value, m, include) }
        }


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Maximum(
            val maximum: String,
            val include: Boolean = true,
        ) {
            data class MoreMaximumBigDecimal<T : BigDecimalValue<T>>(
                override val kClass: KClass<out T>,
                val actual: BigDecimal,
                val minimum: BigDecimal,
                val include: Boolean
            ) : InvalidDomainValue<T>
        }

        private fun <V : BigDecimalValue<V>> Maximum.validate(target: V): Maximum.MoreMaximumBigDecimal<V>? {
            val max = maximum.toBigDecimal()
            return target.takeIf { max < it.value || (!include && it.value == max) }
                ?.let { Maximum.MoreMaximumBigDecimal(it::class, it.value, max, include) }
        }
    }
}

inline fun <V : BigDecimalValue<V>, reified R : BigDecimalValue<R>> V.to(): R = R::class.primaryConstructor!!.call(value)
