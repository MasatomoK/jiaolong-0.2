package com.masatomo.jiaolong.core.common

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import com.masatomo.jiaolong.core.validation.AnnotatedValueValidator
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


interface LongValue<V : LongValue<V>> : InlineDomainValue<Long, V> {

    companion object {
        operator fun invoke(value: Long) = Anonymous(value)
    }

    @JvmInline
    value class Anonymous(override val value: Long) : LongValue<Anonymous> {
        override fun isValid(): Collection<InvalidDomainValue<Anonymous>> = emptyList()
    }

    override val annotatedValidators: Array<AnnotatedValueValidator<V>>
        get() = Validations.allValidator()


    object Validations {
        @Suppress("UNCHECKED_CAST")
        fun <V : LongValue<V>> allValidator(): Array<AnnotatedValueValidator<V>> = arrayOf(
            Positive::class to { (this as Positive).validate(it as V) },
            Negative::class to { (this as Negative).validate(it as V) },
            Minimum::class to { (this as Minimum).validate(it as V) },
            Maximum::class to { (this as Maximum).validate(it as V) },
            Even::class to { (this as Even).validate(it as V) },
            NotOddLong::class to { (this as NotOddLong).validate(it as V) },
        )


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Positive {
            data class NotPositiveLong<T : LongValue<T>>(
                override val kClass: KClass<out T>,
                val actual: Long,
            ) : InvalidDomainValue<T>
        }

        private fun <V : LongValue<V>> Positive.validate(target: V): Positive.NotPositiveLong<V>? =
            target.takeIf { it.value <= 0 }
                ?.let { Positive.NotPositiveLong(it::class, it.value) }


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Negative {
            data class NotNegativeLong<T : LongValue<T>>(
                override val kClass: KClass<out T>,
                val actual: Long,
            ) : InvalidDomainValue<T>
        }

        private fun <V : LongValue<V>> Negative.validate(target: V): Negative.NotNegativeLong<V>? =
            target.takeIf { it.value <= 0 }
                ?.let { Negative.NotNegativeLong(it::class, it.value) }


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Minimum(
            val minimum: Long = Long.MIN_VALUE,
            val include: Boolean = true,
        ) {
            data class LessMinimumLong<T : LongValue<T>>(
                override val kClass: KClass<out T>,
                val actual: Long,
                val minimum: Long,
                val include: Boolean
            ) : InvalidDomainValue<T>
        }

        private fun <V : LongValue<V>> Minimum.validate(target: V): Minimum.LessMinimumLong<V>? =
            target.takeIf { it.value < minimum || (!include && it.value == minimum) }
                ?.let { Minimum.LessMinimumLong(it::class, it.value, minimum, include) }


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Maximum(
            val maximum: Long = Long.MAX_VALUE,
            val include: Boolean = true,
        ) {
            data class MoreMaximumLong<T : LongValue<T>>(
                override val kClass: KClass<out T>,
                val actual: Long,
                val minimum: Long,
                val include: Boolean
            ) : InvalidDomainValue<T>
        }

        private fun <V : LongValue<V>> Maximum.validate(target: V): Maximum.MoreMaximumLong<V>? =
            target.takeIf { maximum < it.value || (!include && it.value == maximum) }
                ?.let { Maximum.MoreMaximumLong(it::class, it.value, maximum, include) }


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class Even {
            data class NotEvenLong<T : LongValue<T>>(
                override val kClass: KClass<out T>,
                val actual: Long,
            ) : InvalidDomainValue<T>
        }

        private fun <V : LongValue<V>> Even.validate(target: V): Even.NotEvenLong<V>? =
            target.takeIf { it.value % 2 == 1L }
                ?.let { Even.NotEvenLong(it::class, it.value) }


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class NotOddLong {
            data class Invalid<T : LongValue<T>>(
                override val kClass: KClass<out T>,
                val actual: Long,
            ) : InvalidDomainValue<T>
        }

        private fun <V : LongValue<V>> NotOddLong.validate(target: V): NotOddLong.Invalid<V>? =
            target.takeIf { it.value % 2 == 0L }
                ?.let { NotOddLong.Invalid(it::class, it.value) }
    }
}

inline fun <V : LongValue<V>, reified R : LongValue<R>> V.to(): R = R::class.primaryConstructor!!.call(value)
