package com.masatomo.jiaolong.core.validation.values

import com.masatomo.jiaolong.core.domain.values.StringValue
import com.masatomo.jiaolong.core.validation.InvalidValidationException
import com.masatomo.jiaolong.core.validation.validate
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual

class StringValueTest : FunSpec({
    context("MinimumLengthString") {
        test("XXX has more than 3 characters") {
            shouldNotThrow<InvalidValidationException> {
                MinimumLengthStringValueTestee("X".repeat(3))
            }
        }

        test("XX does not have more than 3 characters") {
            shouldThrow<InvalidValidationException> {
                MinimumLengthStringValueTestee("X".repeat(2))
            }.apply {
                kClass shouldBeEqual MinimumLengthStringValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual ShortLengthString(MinimumLengthStringValueTestee::class, "X".repeat(2), 3)
            }
        }
    }

    context("MaximumLengthString") {
        test("XXX has less than 5 characters") {
            shouldNotThrow<InvalidValidationException> {
                MaximumLengthStringValueTestee("X".repeat(5))
            }
        }

        test("XXXXXX does not have more than 5 characters") {
            shouldThrow<InvalidValidationException> {
                MaximumLengthStringValueTestee("X".repeat(6))
            }.apply {
                kClass shouldBeEqual MaximumLengthStringValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual LongLengthString(MaximumLengthStringValueTestee::class, "X".repeat(6), 5)
            }
        }
    }

    context("MatchRegexString") {
        test("abcdefghijklmnopqrstuvwxyz has only lower cases") {
            shouldNotThrow<InvalidValidationException> {
                MatchRegexStringValueTestee("abcdefghijklmnopqrstuvwxyz")
            }
        }

        test("Abcdefghijklmnopqrstuvwxyz has only lower cases") {
            shouldThrow<InvalidValidationException> {
                MatchRegexStringValueTestee("Abcdefghijklmnopqrstuvwxyz")
            }.apply {
                kClass shouldBeEqual MatchRegexStringValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotMatchRegexString(
                    MatchRegexStringValueTestee::class,
                    "Abcdefghijklmnopqrstuvwxyz",
                    "LOWER_CASE",
                    "[a-z]*"
                )
            }
        }
    }
})

private class MinimumLengthStringValueTestee(override val value: String) : StringValue {
    init {
        validate(this) {
            minimumLength(3)
        }
    }
}

private class MaximumLengthStringValueTestee(override val value: String) : StringValue {
    init {
        validate(this) {
            maximumLength(5)
        }
    }
}

private class MatchRegexStringValueTestee(override val value: String) : StringValue {
    init {
        validate(this) {
            matchRegex(InstalledPattern.LOWER_CASE)
        }
    }
}
