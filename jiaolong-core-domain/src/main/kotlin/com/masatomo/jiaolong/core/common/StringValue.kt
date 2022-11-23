package com.masatomo.jiaolong.core.common

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import com.masatomo.jiaolong.core.validation.toValidator
import com.masatomo.jiaolong.core.validation.validateBy
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor


interface StringValue : InlineDomainValue<String> {

    companion object {
        operator fun invoke(value: String) = Anonymous(value)

        @JvmInline
        value class Anonymous(override val value: String) : StringValue
    }

    override fun isValid() = validateBy(*StringValidations.predefinedValidators)


    object StringValidations {
        val predefinedValidators = arrayOf(
            MinimumLength::class.toValidator(MinimumLength::validate),
            MaximumLength::class.toValidator(MaximumLength::validate),
            MatchRegex::class.toValidator(MatchRegex::validate),
        )

        @Target(AnnotationTarget.CLASS)
        annotation class MinimumLength(
            val limit: Int
        )

        @Target(AnnotationTarget.CLASS)
        annotation class MaximumLength(
            val limit: Int
        )

        /**
         *  typeがMANUALの場合のみname, customの設定が使用される。
         */
        @Target(AnnotationTarget.CLASS)
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
                ;
            }
        }
    }
}

private fun <V : StringValue> StringValue.StringValidations.MinimumLength.validate(target: V): ShortLengthString<V>? =
    target.takeIf { it.value.length < limit }
        ?.let { ShortLengthString(it::class, it.value, limit) }

data class ShortLengthString<V : StringValue>(
    override val kClass: KClass<out V>,
    val actual: String,
    val limit: Int
) : InvalidDomainValue

private fun <V : StringValue> StringValue.StringValidations.MaximumLength.validate(target: V): LongLengthString<V>? =
    target.takeIf { target.value.length < limit }
        ?.let { LongLengthString(it::class, it.value, limit) }


data class LongLengthString<V : StringValue>(
    override val kClass: KClass<out V>,
    val actual: String,
    val limit: Int
) : InvalidDomainValue


private fun <V : StringValue> StringValue.StringValidations.MatchRegex.validate(target: V): UnmatchRegexString<V>? {
    val (name, regex) = if (type == StringValue.StringValidations.MatchRegex.InstalledPattern.MANUAL) {
        name to regexCache.computeIfAbsent(pattern) { Regex(it) }
    } else {
        type.name.lowercase() to regexCache.computeIfAbsent(type.pattern) { Regex(it) }
    }
    return target.takeIf { !regex.matches(it.value) }
        ?.let { UnmatchRegexString(it::class, it.value, name, pattern) }
}

data class UnmatchRegexString<V : StringValue>(
    override val kClass: KClass<out V>,
    val actual: String,
    val name: String,
    val pattern: String
) : InvalidDomainValue

private val regexCache = mutableMapOf<String, Regex>()


inline fun <V : StringValue, reified R : StringValue> V.to(): R = R::class.primaryConstructor!!.call(value)
