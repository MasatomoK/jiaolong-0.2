package com.masatomo.jiaolong.lib.numeric

import com.masatomo.jiaolong.lib.numeric.algebra.Rational
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class RationalTest : FunSpec({

    context("plus") {
        test("just add") {
            Rational(2, 3) + Rational(3, 2) shouldBe Rational(13, 6)
        }

        test("with reduce") {
            Rational(1, 2) + Rational(1, 6) shouldBe Rational(2, 3)
        }

        test("0") {
            Rational(0, 2) + Rational(0, 6) shouldBe Rational(0)
        }
    }


    context("minus") {
        test("just minus") {
            Rational(3, 2) - Rational(2, 3) shouldBe Rational(5, 6)
        }

        test("with reduce") {
            Rational(1, 2) - Rational(1, 6) shouldBe Rational(1, 3)
        }
    }

    context("times") {
        test("just times") {
            Rational(1, 2) * Rational(3, 4) shouldBe Rational(3, 8)
        }

        test("with reduce") {
            Rational(1, 2) * Rational(4, 3) shouldBe Rational(2, 3)
        }
    }

    context("div") {

    }

    context("pow") {
        test("just pow") {
            Rational(2, 3).pow(Rational(3)) shouldBe Rational(8, 27)
        }
    }
})
