package com.masatomo.jiaolong.core.validation.values

import com.masatomo.jiaolong.core.domain.values.BigDecimalValue
import com.masatomo.jiaolong.core.validation.InvalidValidationException
import com.masatomo.jiaolong.core.validation.validate
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual
import java.math.BigDecimal

class BigDecimalValueTest : FunSpec({
    context("PositiveBigDecimal") {
        test("1 is positive number") {
            shouldNotThrow<InvalidValidationException> {
                PositiveBigDecimalValueTestee(BigDecimal(1))
            }
        }

        test("0 is not positive number") {
            shouldThrow<InvalidValidationException> { PositiveBigDecimalValueTestee(BigDecimal(0)) }.apply {
                kClass shouldBeEqual PositiveBigDecimalValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotPositiveBigDecimal(PositiveBigDecimalValueTestee::class, BigDecimal(0))
            }
        }
    }

    context("NegativeBigDecimal") {
        test("-1 is negative number") {
            shouldNotThrow<InvalidValidationException> {
                NegativeBigDecimalValueTestee(BigDecimal(-1))
            }
        }

        test("0 is not negative number") {
            shouldThrow<InvalidValidationException> { NegativeBigDecimalValueTestee(BigDecimal(0)) }.apply {
                kClass shouldBeEqual NegativeBigDecimalValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotNegativeBigDecimal(NegativeBigDecimalValueTestee::class, BigDecimal(0))
            }
        }
    }

    context("MinimumIncludeBigDecimal") {
        test("15 is accepted with minimum 15 and `include=true`") {
            shouldNotThrow<InvalidValidationException> {
                MinimumIncludeBigDecimalValueTestee(BigDecimal(15))
            }
        }

        test("14 is not more than 15") {
            shouldThrow<InvalidValidationException> { MinimumIncludeBigDecimalValueTestee(BigDecimal(14)) }.apply {
                kClass shouldBeEqual MinimumIncludeBigDecimalValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual LessMinimumBigDecimal(
                    MinimumIncludeBigDecimalValueTestee::class,
                    BigDecimal(14),
                    BigDecimal(15),
                    true
                )
            }
        }
    }

    context("MinimumExcludeBigDecimal") {
        test("16 is more than 15") {
            shouldNotThrow<InvalidValidationException> {
                MinimumExcludeBigDecimalValueTestee(BigDecimal(16))
            }
        }

        test("15 is not more than 15") {
            shouldThrow<InvalidValidationException> { MinimumExcludeBigDecimalValueTestee(BigDecimal(15)) }.apply {
                kClass shouldBeEqual MinimumExcludeBigDecimalValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual LessMinimumBigDecimal(
                    MinimumExcludeBigDecimalValueTestee::class,
                    BigDecimal(15),
                    BigDecimal(15),
                    false
                )
            }
        }
    }

    context("MaximumIncludeBigDecimal") {
        test("15 is accepted with maximum 15 and `include=true`") {
            shouldNotThrow<InvalidValidationException> {
                MaximumIncludeBigDecimalValueTestee(BigDecimal(15))
            }
        }

        test("16 is not less than 15") {
            shouldThrow<InvalidValidationException> { MaximumIncludeBigDecimalValueTestee(BigDecimal(16)) }.apply {
                kClass shouldBeEqual MaximumIncludeBigDecimalValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual MoreMaximumBigDecimal(
                    MaximumIncludeBigDecimalValueTestee::class,
                    BigDecimal(16),
                    BigDecimal(15),
                    true
                )
            }
        }
    }

    context("MaximumExcludeBigDecimal") {
        test("14 is less than 15") {
            shouldNotThrow<InvalidValidationException> {
                MaximumExcludeBigDecimalValueTestee(BigDecimal(14))
            }
        }

        test("15 is not less than 15") {
            shouldThrow<InvalidValidationException> { MaximumExcludeBigDecimalValueTestee(BigDecimal(15)) }.apply {
                kClass shouldBeEqual MaximumExcludeBigDecimalValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual MoreMaximumBigDecimal(
                    MaximumExcludeBigDecimalValueTestee::class,
                    BigDecimal(15),
                    BigDecimal(15),
                    false
                )
            }
        }
    }
})

private class PositiveBigDecimalValueTestee(override val value: BigDecimal) : BigDecimalValue {
    init {
        validate {
            positive()
        }
    }
}

private class NegativeBigDecimalValueTestee(override val value: BigDecimal) : BigDecimalValue {
    init {
        validate {
            negative()
        }
    }
}

private class MinimumIncludeBigDecimalValueTestee(override val value: BigDecimal) : BigDecimalValue {
    init {
        validate {
            minimumWith(BigDecimal(15), true)
        }
    }
}

private class MinimumExcludeBigDecimalValueTestee(override val value: BigDecimal) : BigDecimalValue {
    init {
        validate {
            minimumWith(BigDecimal(15), false)
        }
    }
}

private class MaximumIncludeBigDecimalValueTestee(override val value: BigDecimal) : BigDecimalValue {
    init {
        validate {
            maximumWith(BigDecimal(15), true)
        }
    }
}

private class MaximumExcludeBigDecimalValueTestee(override val value: BigDecimal) : BigDecimalValue {
    init {
        validate {
            maximumWith(BigDecimal(15), false)
        }
    }
}
