package com.masatomo.jiaolong.core.validation.values

import com.masatomo.jiaolong.core.domain.values.StringValue
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import com.masatomo.jiaolong.core.validation.ValidationContext
import kotlin.reflect.KClass


fun <V : StringValue> ValidationContext<V>.minimumLength(limit: Int) = target.takeIf {
    it.value.length < limit
}?.apply { addReason(ShortLengthString(this::class, value, limit)) }

data class ShortLengthString<V : StringValue>(
    override val kClass: KClass<out V>,
    val actual: String,
    val limit: Int
) : InvalidDomainValue

fun <V : StringValue> ValidationContext<V>.maximumLength(limit: Int) = target.takeIf {
    limit < target.value.length
}?.apply { addReason(LongLengthString(this::class, value, limit)) }

data class LongLengthString<V : StringValue>(
    override val kClass: KClass<out V>,
    val actual: String,
    val limit: Int
) : InvalidDomainValue


fun <V : StringValue> ValidationContext<V>.matchRegex(
    preDefined: InstalledPattern
) = matchRegex(preDefined.name, preDefined.regex)

fun <V : StringValue> ValidationContext<V>.matchRegex(
    simpleName: String,
    regex: Regex,
) = target.takeIf {
    !regex.matches(it.value)
}?.apply { addReason(NotMatchRegexString(this::class, value, simpleName, regex.pattern)) }

data class NotMatchRegexString<V : StringValue>(
    override val kClass: KClass<out V>,
    val actual: String,
    val name: String,
    val pattern: String
) : InvalidDomainValue

enum class InstalledPattern(val regex: Regex) {
    ANY(Regex(".*")),
    UPPER_CASES(Regex("[A-Z]*")),
    LOWER_CASE(Regex("[a-z]*")),
}
