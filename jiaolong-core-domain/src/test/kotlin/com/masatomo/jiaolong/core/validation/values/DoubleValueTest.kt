package com.masatomo.jiaolong.core.validation.values

import com.masatomo.jiaolong.core.domain.values.DoubleValue
import com.masatomo.jiaolong.core.validation.InvalidValidationException
import com.masatomo.jiaolong.core.validation.validate
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual

class DoubleValueTest : FunSpec({
    context("PositiveDouble") {
        test("1 is positive number") {
            shouldNotThrow<InvalidValidationException> {
                PositiveDoubleValueTestee(1.0)
            }
        }

        test("0 is not positive number") {
            shouldThrow<InvalidValidationException> { PositiveDoubleValueTestee(0.0) }.apply {
                kClass shouldBeEqual PositiveDoubleValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotPositiveDouble(PositiveDoubleValueTestee::class, 0.0)
            }
        }
    }

    context("NegativeDouble") {
        test("-1 is negative number") {
            shouldNotThrow<InvalidValidationException> {
                NegativeDoubleValueTestee(-1.0)
            }
        }

        test("0 is not negative number") {
            shouldThrow<InvalidValidationException> { NegativeDoubleValueTestee(0.0) }.apply {
                kClass shouldBeEqual NegativeDoubleValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotNegativeDouble(NegativeDoubleValueTestee::class, 0.0)
            }
        }
    }

    context("MinimumIncludeDouble") {
        test("15 is accepted with minimum 15 and `include=true`") {
            shouldNotThrow<InvalidValidationException> {
                MinimumIncludeDoubleValueTestee(15.0)
            }
        }

        test("14 is not more than 15") {
            shouldThrow<InvalidValidationException> { MinimumIncludeDoubleValueTestee(14.0) }.apply {
                kClass shouldBeEqual MinimumIncludeDoubleValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual LessMinimumDouble(
                    MinimumIncludeDoubleValueTestee::class,
                    14.0,
                    15.0,
                    true
                )
            }
        }
    }

    context("MinimumExcludeDouble") {
        test("16 is more than 15") {
            shouldNotThrow<InvalidValidationException> {
                MinimumExcludeDoubleValueTestee(16.0)
            }
        }

        test("15 is not more than 15") {
            shouldThrow<InvalidValidationException> { MinimumExcludeDoubleValueTestee(15.0) }.apply {
                kClass shouldBeEqual MinimumExcludeDoubleValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual LessMinimumDouble(
                    MinimumExcludeDoubleValueTestee::class,
                    15.0,
                    15.0,
                    false
                )
            }
        }
    }

    context("MaximumIncludeDouble") {
        test("15 is accepted with maximum 15 and `include=true`") {
            shouldNotThrow<InvalidValidationException> {
                MaximumIncludeDoubleValueTestee(15.0)
            }
        }

        test("16 is not less than 15") {
            shouldThrow<InvalidValidationException> { MaximumIncludeDoubleValueTestee(16.0) }.apply {
                kClass shouldBeEqual MaximumIncludeDoubleValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual MoreMaximumDouble(
                    MaximumIncludeDoubleValueTestee::class,
                    16.0,
                    15.0,
                    true
                )
            }
        }
    }

    context("MaximumExcludeDouble") {
        test("14 is less than 15") {
            shouldNotThrow<InvalidValidationException> {
                MaximumExcludeDoubleValueTestee(14.0)
            }
        }

        test("15 is not less than 15") {
            shouldThrow<InvalidValidationException> { MaximumExcludeDoubleValueTestee(15.0) }.apply {
                kClass shouldBeEqual MaximumExcludeDoubleValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual MoreMaximumDouble(
                    MaximumExcludeDoubleValueTestee::class,
                    15.0,
                    15.0,
                    false
                )
            }
        }
    }
})

private class PositiveDoubleValueTestee(override val value: Double) : DoubleValue {
    init {
        validate {
            positive()
        }
    }
}

private class NegativeDoubleValueTestee(override val value: Double) : DoubleValue {
    init {
        validate {
            negative()
        }
    }
}

private class MinimumIncludeDoubleValueTestee(override val value: Double) : DoubleValue {
    init {
        validate {
            minimumWith(15.0, true)
        }
    }
}

private class MinimumExcludeDoubleValueTestee(override val value: Double) : DoubleValue {
    init {
        validate {
            minimumWith(15.0, false)
        }
    }
}

private class MaximumIncludeDoubleValueTestee(override val value: Double) : DoubleValue {
    init {
        validate {
            maximumWith(15.0, true)
        }
    }
}

private class MaximumExcludeDoubleValueTestee(override val value: Double) : DoubleValue {
    init {
        validate {
            maximumWith(15.0, false)
        }
    }
}
