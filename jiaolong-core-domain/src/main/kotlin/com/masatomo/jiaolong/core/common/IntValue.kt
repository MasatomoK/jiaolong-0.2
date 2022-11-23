package com.masatomo.jiaolong.core.common

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import com.masatomo.jiaolong.core.validation.AnnotatedValueValidator
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


interface IntValue<V : IntValue<V>> : InlineDomainValue<Int, V> {

    companion object {
        operator fun invoke(value: Int) = Anonymous(value)
    }

    @JvmInline
    value class Anonymous(override val value: Int) : IntValue<Anonymous>


    override val annotatedValidators: Array<AnnotatedValueValidator<V>>
        get() = Validations.allValidator()


    object Validations {
        @Suppress("UNCHECKED_CAST")
        fun <V : IntValue<V>> allValidator(): Array<AnnotatedValueValidator<V>> = arrayOf(
            Positive::class to { (this as Positive).validate(it as V) },
            Negative::class to { (this as Negative).validate(it as V) },
            Minimum::class to { (this as Minimum).validate(it as V) },
            Maximum::class to { (this as Maximum).validate(it as V) },
            Even::class to { (this as Even).validate(it as V) },
            Odd::class to { (this as Odd).validate(it as V) },
        )


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Positive {

            data class NotPositiveInt<T : IntValue<T>>(
                override val kClass: KClass<out T>,
                val actual: Int,
            ) : InvalidDomainValue<T>
        }

        private fun <V : IntValue<V>> Positive.validate(target: V): Positive.NotPositiveInt<V>? =
            target.takeIf { it.value <= 0 }
                ?.let { Positive.NotPositiveInt(it::class, it.value) }


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Negative {
            data class NotNegativeInt<T : IntValue<T>>(
                override val kClass: KClass<out T>,
                val actual: Int,
            ) : InvalidDomainValue<T>
        }

        private fun <V : IntValue<V>> Negative.validate(target: V): Negative.NotNegativeInt<V>? =
            target.takeIf { it.value >= 0 }
                ?.let { Negative.NotNegativeInt(it::class, it.value) }


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Minimum(
            val minimum: Int = Int.MIN_VALUE,
            val include: Boolean = true,
        ) {
            data class LessMinimumInt<T : IntValue<T>>(
                override val kClass: KClass<out T>,
                val actual: Int,
                val minimum: Int,
                val include: Boolean
            ) : InvalidDomainValue<T>
        }

        private fun <V : IntValue<V>> Minimum.validate(target: V): Minimum.LessMinimumInt<V>? =
            target.takeIf { it.value < minimum || (!include && it.value == minimum) }
                ?.let { Minimum.LessMinimumInt(it::class, it.value, minimum, include) }


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Maximum(
            val maximum: Int = Int.MAX_VALUE,
            val include: Boolean = true,
        ) {
            data class MoreMaximumInt<T : IntValue<T>>(
                override val kClass: KClass<out T>,
                val actual: Int,
                val minimum: Int,
                val include: Boolean
            ) : InvalidDomainValue<T>
        }

        private fun <V : IntValue<V>> Maximum.validate(target: V): Maximum.MoreMaximumInt<V>? =
            target.takeIf { maximum < it.value || (!include && it.value == maximum) }
                ?.let { Maximum.MoreMaximumInt(it::class, it.value, maximum, include) }


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Even {
            data class NotEvenInt<T : IntValue<T>>(
                override val kClass: KClass<out T>,
                val actual: Int,
            ) : InvalidDomainValue<T>
        }

        private fun <V : IntValue<V>> Even.validate(target: V): Even.NotEvenInt<V>? =
            target.takeIf { it.value % 2 == 1 }
                ?.let { Even.NotEvenInt(it::class, it.value) }


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Odd {
            data class NotOddInt<T : IntValue<T>>(
                override val kClass: KClass<out T>,
                val actual: Int,
            ) : InvalidDomainValue<T>
        }

        private fun <V : IntValue<V>> Odd.validate(target: V): Odd.NotOddInt<V>? =
            target.takeIf { it.value % 2 == 0 }
                ?.let { Odd.NotOddInt(it::class, it.value) }
    }
}

inline fun <V : IntValue<V>, reified R : IntValue<R>> V.to(): R = R::class.primaryConstructor!!.call(value)
