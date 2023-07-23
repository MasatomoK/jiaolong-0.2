package com.masatomo.jiaolong.core.validation.values

import com.masatomo.jiaolong.core.domain.values.IntValue
import com.masatomo.jiaolong.core.validation.InvalidValidationException
import com.masatomo.jiaolong.core.validation.validate
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.equals.shouldBeEqual

class IntValueTest : FunSpec({
    context("PositiveInt") {
        test("1 is positive number") {
            shouldNotThrow<InvalidValidationException> {
                PositiveIntValueTestee(1)
            }
        }

        test("0 is not positive number") {
            shouldThrow<InvalidValidationException> { PositiveIntValueTestee(0) }.apply {
                kClass shouldBeEqual PositiveIntValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotPositiveInt(PositiveIntValueTestee::class, 0)
            }
        }
    }

    context("NegativeInt") {
        test("-1 is negative number") {
            shouldNotThrow<InvalidValidationException> {
                NegativeIntValueTestee(-1)
            }
        }

        test("0 is not negative number") {
            shouldThrow<InvalidValidationException> { NegativeIntValueTestee(0) }.apply {
                kClass shouldBeEqual NegativeIntValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotNegativeInt(NegativeIntValueTestee::class, 0)
            }
        }
    }

    context("MinimumIncludeInt") {
        test("15 is accepted with minimum 15 and `include=true`") {
            shouldNotThrow<InvalidValidationException> {
                MinimumIncludeIntValueTestee(15)
            }
        }

        test("14 is not more than 15") {
            shouldThrow<InvalidValidationException> { MinimumIncludeIntValueTestee(14) }.apply {
                kClass shouldBeEqual MinimumIncludeIntValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual LessMinimumInt(MinimumIncludeIntValueTestee::class, 14, 15, true)
            }
        }
    }

    context("MinimumExcludeInt") {
        test("16 is more than 15") {
            shouldNotThrow<InvalidValidationException> {
                MinimumExcludeIntValueTestee(16)
            }
        }

        test("15 is not more than 15") {
            shouldThrow<InvalidValidationException> { MinimumExcludeIntValueTestee(15) }.apply {
                kClass shouldBeEqual MinimumExcludeIntValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual LessMinimumInt(MinimumExcludeIntValueTestee::class, 15, 15, false)
            }
        }
    }

    context("MaximumIncludeInt") {
        test("15 is accepted with maximum 15 and `include=true`") {
            shouldNotThrow<InvalidValidationException> {
                MaximumIncludeIntValueTestee(15)
            }
        }

        test("16 is not less than 15") {
            shouldThrow<InvalidValidationException> { MaximumIncludeIntValueTestee(16) }.apply {
                kClass shouldBeEqual MaximumIncludeIntValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual MoreMaximumInt(MaximumIncludeIntValueTestee::class, 16, 15, true)
            }
        }
    }

    context("MaximumExcludeInt") {
        test("14 is less than 15") {
            shouldNotThrow<InvalidValidationException> {
                MaximumExcludeIntValueTestee(14)
            }
        }

        test("15 is not less than 15") {
            shouldThrow<InvalidValidationException> { MaximumExcludeIntValueTestee(15) }.apply {
                kClass shouldBeEqual MaximumExcludeIntValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual MoreMaximumInt(MaximumExcludeIntValueTestee::class, 15, 15, false)
            }
        }
    }

    context("EvenInt") {
        test("0 is even") {
            shouldNotThrow<InvalidValidationException> {
                EvenIntValueTestee(0)
            }
        }

        test("1 is not even") {
            shouldThrow<InvalidValidationException> { EvenIntValueTestee(1) }.apply {
                kClass shouldBeEqual EvenIntValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotEvenInt(EvenIntValueTestee::class, 1)
            }
        }

        test("-1 is not even") {
            shouldThrow<InvalidValidationException> { EvenIntValueTestee(-1) }.apply {
                kClass shouldBeEqual EvenIntValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotEvenInt(EvenIntValueTestee::class, -1)
            }
        }
    }

    context("OddInt") {
        test("1 is odd") {
            shouldNotThrow<InvalidValidationException> {
                OddIntValueTestee(1)
            }
        }

        test("0 is not odd") {
            shouldThrow<InvalidValidationException> { OddIntValueTestee(0) }.apply {
                kClass shouldBeEqual OddIntValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotOddInt(OddIntValueTestee::class, 0)
            }
        }
    }

    context("EvenAndMinimumInt (MultiValidation)") {
        test("0 is even and satisfy minimum 0") {
            shouldNotThrow<InvalidValidationException> {
                EvenAndMinimumIntValueTestee(0)
            }
        }

        test("1 is not even but satisfy minimum 0") {
            shouldThrow<InvalidValidationException> { EvenAndMinimumIntValueTestee(1) }.apply {
                kClass shouldBeEqual EvenAndMinimumIntValueTestee::class
                reasons.size shouldBeEqual 1
                reasons.first() shouldBeEqual NotEvenInt(EvenAndMinimumIntValueTestee::class, 1)
            }
        }

        test("-1 is not even and not satisfy minimum 0") {
            shouldThrow<InvalidValidationException> { EvenAndMinimumIntValueTestee(-1) }.apply {
                kClass shouldBeEqual EvenAndMinimumIntValueTestee::class
                reasons.size shouldBeEqual 2
                reasons[0] shouldBeEqual LessMinimumInt(EvenAndMinimumIntValueTestee::class, -1, 0, true)
                reasons[1] shouldBeEqual NotEvenInt(EvenAndMinimumIntValueTestee::class, -1)
            }
        }
    }
})

private class PositiveIntValueTestee(override val value: Int) : IntValue {
    init {
        validate {
            positive()
        }
    }
}

private class NegativeIntValueTestee(override val value: Int) : IntValue {
    init {
        validate {
            negative()
        }
    }
}

private class MinimumIncludeIntValueTestee(override val value: Int) : IntValue {
    init {
        validate {
            minimumWith(15, true)
        }
    }
}

private class MinimumExcludeIntValueTestee(override val value: Int) : IntValue {
    init {
        validate {
            minimumWith(15, false)
        }
    }
}

private class MaximumIncludeIntValueTestee(override val value: Int) : IntValue {
    init {
        validate {
            maximumWith(15, true)
        }
    }
}

private class MaximumExcludeIntValueTestee(override val value: Int) : IntValue {
    init {
        validate {
            maximumWith(15, false)
        }
    }
}

private class EvenIntValueTestee(override val value: Int) : IntValue {
    init {
        validate {
            even()
        }
    }
}

private class OddIntValueTestee(override val value: Int) : IntValue {
    init {
        validate {
            odd()
        }
    }
}

private class EvenAndMinimumIntValueTestee(override val value: Int) : IntValue {
    init {
        validate {
            minimumWith(0, true)
            even()
        }
    }
}
