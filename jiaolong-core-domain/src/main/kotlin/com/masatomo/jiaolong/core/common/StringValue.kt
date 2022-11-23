package com.masatomo.jiaolong.core.common

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import com.masatomo.jiaolong.core.validation.AnnotatedValueValidator
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


interface StringValue<V : StringValue<V>> : InlineDomainValue<String, V> {

    companion object {
        operator fun invoke(value: String) = Anonymous(value)
    }

    @JvmInline
    value class Anonymous(override val value: String) : StringValue<Anonymous> {
        override fun isValid(): Collection<InvalidDomainValue<Anonymous>> = emptyList()
    }


    override val annotatedValidators: Array<AnnotatedValueValidator<V>>
        get() = StringValidations.allValidator()


    object StringValidations {
        @Suppress("UNCHECKED_CAST")
        fun <V : StringValue<V>> allValidator(): Array<AnnotatedValueValidator<V>> = arrayOf(
            MinimumLength::class to { (this as MinimumLength).validate(it as V) },
            MaximumLength::class to { (this as MaximumLength).validate(it as V) },
            MatchRegex::class to { (this as MatchRegex).validate(it as V) },
        )


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class MinimumLength(
            val limit: Int
        )

        private fun <V : StringValue<V>> MinimumLength.validate(target: V): ShortLengthString<V>? =
            target.takeIf { it.value.length < limit }
                ?.let { ShortLengthString(it::class, it.value, limit) }


        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class MaximumLength(
            val limit: Int
        )

        private fun <V : StringValue<V>> MaximumLength.validate(target: V): LongLengthString<V>? =
            target.takeIf { target.value.length < limit }
                ?.let { LongLengthString(it::class, it.value, limit) }


        private val regexCache = mutableMapOf<String, Regex>()

        /**
         *  typeがMANUALの場合のみname, customの設定が使用される。
         */
        @Target(AnnotationTarget.CLASS)
        @Retention(AnnotationRetention.RUNTIME)
        annotation class MatchRegex(
            val type: InstalledPattern,
            val name: String = "",
            val pattern: String = "",
        ) {
            enum class InstalledPattern(val pattern: String) {
                MANUAL(""),  // 本当はRegexPattern自体をSealedClassにしたいが、annotationで使用するためやむなし。
                ANY(".*"),
                UPPER_CASES("[A-Z]*"),
                LOWER_CASE("[a-z]*"),
                // TODO
                ;
            }
        }

        private fun <V : StringValue<V>> MatchRegex.validate(target: V): UnmatchRegexString<V>? {
            val (name, regex) = if (type == MatchRegex.InstalledPattern.MANUAL) {
                name to regexCache.computeIfAbsent(pattern) { Regex(it) }
            } else {
                type.name.lowercase() to regexCache.computeIfAbsent(type.pattern) { Regex(it) }
            }
            return target.takeIf { !regex.matches(it.value) }
                ?.let { UnmatchRegexString(it::class, it.value, name, pattern) }
        }
    }


    data class ShortLengthString<V : StringValue<V>>(
        override val kClass: KClass<out V>,
        val actual: String,
        val limit: Int
    ) : InvalidDomainValue<V>

    data class LongLengthString<V : StringValue<V>>(
        override val kClass: KClass<out V>,
        val actual: String,
        val limit: Int
    ) : InvalidDomainValue<V>

    data class UnmatchRegexString<V : StringValue<V>>(
        override val kClass: KClass<out V>,
        val actual: String,
        val name: String,
        val pattern: String
    ) : InvalidDomainValue<V>
}

inline fun <V : StringValue<V>, reified R : StringValue<R>> V.to(): R = R::class.primaryConstructor!!.call(value)
