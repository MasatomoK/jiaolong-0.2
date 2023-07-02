package com.masatomo.jiaolong.core.validation.values

import com.masatomo.jiaolong.core.domain.values.LongValue
import com.masatomo.jiaolong.core.validation.InvalidValidationException
import com.masatomo.jiaolong.core.validation.validate
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual

class LongValueTest : FunSpec({
    context("PositiveLong") {
        test("1 is positive number") {
            shouldNotThrow<InvalidValidationException> {
                PositiveLongValueTestee(1)
            }
        }

        test("0 is not positive number") {
            shouldThrow<InvalidValidationException> { PositiveLongValueTestee(0) }.apply {
                kClass shouldBeEqual PositiveLongValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotPositiveLong(PositiveLongValueTestee::class, 0)
            }
        }
    }

    context("NegativeLong") {
        test("-1 is negative number") {
            shouldNotThrow<InvalidValidationException> {
                NegativeLongValueTestee(-1)
            }
        }

        test("0 is not negative number") {
            shouldThrow<InvalidValidationException> { NegativeLongValueTestee(0) }.apply {
                kClass shouldBeEqual NegativeLongValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotNegativeLong(NegativeLongValueTestee::class, 0)
            }
        }
    }

    context("MinimumIncludeLong") {
        test("15 is accepted with minimum 15 and `include=true`") {
            shouldNotThrow<InvalidValidationException> {
                MinimumIncludeLongValueTestee(15)
            }
        }

        test("14 is not more than 15") {
            shouldThrow<InvalidValidationException> { MinimumIncludeLongValueTestee(14) }.apply {
                kClass shouldBeEqual MinimumIncludeLongValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual LessMinimumLong(MinimumIncludeLongValueTestee::class, 14, 15, true)
            }
        }
    }

    context("MinimumExcludeLong") {
        test("16 is more than 15") {
            shouldNotThrow<InvalidValidationException> {
                MinimumExcludeLongValueTestee(16)
            }
        }

        test("15 is not more than 15") {
            shouldThrow<InvalidValidationException> { MinimumExcludeLongValueTestee(15) }.apply {
                kClass shouldBeEqual MinimumExcludeLongValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual LessMinimumLong(MinimumExcludeLongValueTestee::class, 15, 15, false)
            }
        }
    }

    context("MaximumIncludeLong") {
        test("15 is accepted with maximum 15 and `include=true`") {
            shouldNotThrow<InvalidValidationException> {
                MaximumIncludeLongValueTestee(15)
            }
        }

        test("16 is not less than 15") {
            shouldThrow<InvalidValidationException> { MaximumIncludeLongValueTestee(16) }.apply {
                kClass shouldBeEqual MaximumIncludeLongValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual MoreMaximumLong(MaximumIncludeLongValueTestee::class, 16, 15, true)
            }
        }
    }

    context("MaximumExcludeLong") {
        test("14 is less than 15") {
            shouldNotThrow<InvalidValidationException> {
                MaximumExcludeLongValueTestee(14)
            }
        }

        test("15 is not less than 15") {
            shouldThrow<InvalidValidationException> { MaximumExcludeLongValueTestee(15) }.apply {
                kClass shouldBeEqual MaximumExcludeLongValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual MoreMaximumLong(MaximumExcludeLongValueTestee::class, 15, 15, false)
            }
        }
    }

    context("EvenLong") {
        test("0 is even") {
            shouldNotThrow<InvalidValidationException> {
                EvenLongValueTestee(0)
            }
        }

        test("1 is not even") {
            shouldThrow<InvalidValidationException> { EvenLongValueTestee(1) }.apply {
                kClass shouldBeEqual EvenLongValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotEvenLong(EvenLongValueTestee::class, 1)
            }
        }

        test("-1 is not even") {
            shouldThrow<InvalidValidationException> { EvenLongValueTestee(-1) }.apply {
                kClass shouldBeEqual EvenLongValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotEvenLong(EvenLongValueTestee::class, -1)
            }
        }
    }

    context("OddLong") {
        test("1 is odd") {
            shouldNotThrow<InvalidValidationException> {
                OddLongValueTestee(1)
            }
        }

        test("0 is not odd") {
            shouldThrow<InvalidValidationException> { OddLongValueTestee(0) }.apply {
                kClass shouldBeEqual OddLongValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotOddLong(OddLongValueTestee::class, 0)
            }
        }
    }

    context("EvenAndMinimumLong (MultiValidation)") {
        test("0 is even and satisfy minimum 0") {
            shouldNotThrow<InvalidValidationException> {
                EvenAndMinimumLongValueTestee(0)
            }
        }

        test("1 is not even but satisfy minimum 0") {
            shouldThrow<InvalidValidationException> { EvenAndMinimumLongValueTestee(1) }.apply {
                kClass shouldBeEqual EvenAndMinimumLongValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotEvenLong(EvenAndMinimumLongValueTestee::class, 1)
            }
        }

        test("-1 is not even and not satisfy minimum 0") {
            shouldThrow<InvalidValidationException> { EvenAndMinimumLongValueTestee(-1) }.apply {
                kClass shouldBeEqual EvenAndMinimumLongValueTestee::class
                reasons.size shouldBeEqual 2
                reasons[0] shouldBeEqual LessMinimumLong(EvenAndMinimumLongValueTestee::class, -1, 0, true)
                reasons[1] shouldBeEqual NotEvenLong(EvenAndMinimumLongValueTestee::class, -1)
            }
        }
    }
})

private class PositiveLongValueTestee(override val value: Long) : LongValue {
    init {
        validate(this) {
            positive()
        }
    }
}

private class NegativeLongValueTestee(override val value: Long) : LongValue {
    init {
        validate(this) {
            negative()
        }
    }
}

private class MinimumIncludeLongValueTestee(override val value: Long) : LongValue {
    init {
        validate(this) {
            minimumWith(15, true)
        }
    }
}

private class MinimumExcludeLongValueTestee(override val value: Long) : LongValue {
    init {
        validate(this) {
            minimumWith(15, false)
        }
    }
}

private class MaximumIncludeLongValueTestee(override val value: Long) : LongValue {
    init {
        validate(this) {
            maximumWith(15, true)
        }
    }
}

private class MaximumExcludeLongValueTestee(override val value: Long) : LongValue {
    init {
        validate(this) {
            maximumWith(15, false)
        }
    }
}

private class EvenLongValueTestee(override val value: Long) : LongValue {
    init {
        validate(this) {
            even()
        }
    }
}

private class OddLongValueTestee(override val value: Long) : LongValue {
    init {
        validate(this) {
            odd()
        }
    }
}

private class EvenAndMinimumLongValueTestee(override val value: Long) : LongValue {
    init {
        validate(this) {
            minimumWith(0, true)
            even()
        }
    }
}
