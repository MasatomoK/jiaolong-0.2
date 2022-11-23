package com.masatomo.jiaolong.core.common

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import com.masatomo.jiaolong.core.validation.AnnotatedValueValidator
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


interface DoubleValue<V : DoubleValue<V>> : InlineDomainValue<Double, V> {

    companion object {
        operator fun invoke(value: Double) = Anonymous(value)
    }

    @JvmInline
    value class Anonymous(override val value: Double) : DoubleValue<Anonymous> {
        override fun isValid(): Collection<InvalidDomainValue<Anonymous>> = emptyList()
    }

    override val annotatedValidators: Array<AnnotatedValueValidator<V>>
        get() = DoubleValidations.allValidator()


    object DoubleValidations {

        @Suppress("UNCHECKED_CAST")
        fun <V : DoubleValue<V>> allValidator(): Array<AnnotatedValueValidator<V>> = arrayOf(
            Positive::class to { (this as Positive).validate(it as V) },
            Negative::class to { (this as Negative).validate(it as V) },
            Minimum::class to { (this as Minimum).validate(it as V) },
            Maximum::class to { (this as Maximum).validate(it as V) },
        )


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Positive {
            data class NotPositiveDouble<T : DoubleValue<T>>(
                override val kClass: KClass<out T>,
                val actual: Double,
            ) : InvalidDomainValue<T>
        }

        private fun <V : DoubleValue<V>> Positive.validate(target: V): Positive.NotPositiveDouble<V>? =
            target.takeIf { it.value <= 0 }
                ?.let { Positive.NotPositiveDouble(it::class, it.value) }


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Negative {
            data class NotNegativeDouble<T : DoubleValue<T>>(
                override val kClass: KClass<out T>,
                val actual: Double,
            ) : InvalidDomainValue<T>
        }

        private fun <V : DoubleValue<V>> Negative.validate(target: V): Negative.NotNegativeDouble<V>? =
            target.takeIf { it.value <= 0 }
                ?.let { Negative.NotNegativeDouble(it::class, it.value) }


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Minimum(
            val minimum: Double = Double.MIN_VALUE,
            val include: Boolean = true,
        ) {
            data class LessDouble<T : DoubleValue<T>>(
                override val kClass: KClass<out T>,
                val actual: Double,
                val minimum: Double,
                val include: Boolean
            ) : InvalidDomainValue<T>
        }

        private fun <V : DoubleValue<V>> Minimum.validate(target: V): Minimum.LessDouble<V>? =
            target.takeIf { it.value < minimum || (!include && it.value == minimum) }
                ?.let { Minimum.LessDouble(it::class, it.value, minimum, include) }


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Maximum(
            val maximum: Double = Double.MAX_VALUE,
            val include: Boolean = true,
        ) {
            data class MoreDouble<T : DoubleValue<T>>(
                override val kClass: KClass<out T>,
                val actual: Double,
                val minimum: Double,
                val include: Boolean
            ) : InvalidDomainValue<T>
        }

        private fun <V : DoubleValue<V>> Maximum.validate(target: V): Maximum.MoreDouble<V>? =
            target.takeIf { maximum < it.value || (!include && it.value == maximum) }
                ?.let { Maximum.MoreDouble(it::class, it.value, maximum, include) }
    }
}

inline fun <V : DoubleValue<V>, reified R : DoubleValue<R>> V.to(): R = R::class.primaryConstructor!!.call(value)
