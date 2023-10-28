package com.masatomo.jiaolong.lib.numeric

import com.masatomo.jiaolong.lib.numeric.discrete.combination
import com.masatomo.jiaolong.lib.numeric.discrete.orderedCombination
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class CombinationTest : FunSpec({
    test("combination") {
        val testee = combination(3, 1, 2, 3)
        testee.map { it.toList() }.toList() shouldBe listOf(
            listOf(1, 1, 1),
            listOf(1, 1, 2),
            listOf(1, 1, 3),
            listOf(1, 2, 1),
            listOf(1, 2, 2),
            listOf(1, 2, 3),
            listOf(1, 3, 1),
            listOf(1, 3, 2),
            listOf(1, 3, 3),
            listOf(2, 1, 1),
            listOf(2, 1, 2),
            listOf(2, 1, 3),
            listOf(2, 2, 1),
            listOf(2, 2, 2),
            listOf(2, 2, 3),
            listOf(2, 3, 1),
            listOf(2, 3, 2),
            listOf(2, 3, 3),
            listOf(3, 1, 1),
            listOf(3, 1, 2),
            listOf(3, 1, 3),
            listOf(3, 2, 1),
            listOf(3, 2, 2),
            listOf(3, 2, 3),
            listOf(3, 3, 1),
            listOf(3, 3, 2),
            listOf(3, 3, 3),
        )
    }

    test("ordered combination (not duplicate)") {
        val testee = orderedCombination(3, false, 1, 2, 3, 4, 5)
        testee.map { it.toList() }.toList() shouldBe listOf(
            listOf(1, 2, 3),
            listOf(1, 2, 4),
            listOf(1, 2, 5),
            listOf(1, 3, 4),
            listOf(1, 3, 5),
            listOf(1, 4, 5),
            listOf(2, 3, 4),
            listOf(2, 3, 5),
            listOf(2, 4, 5),
            listOf(3, 4, 5),
        )
    }

    test("ordered combination (duplicate)") {
        val testee = orderedCombination(3, true, 1, 2, 3, 4)
        testee.map { it.toList() }.toList() shouldBe listOf(
            listOf(1, 1, 1),
            listOf(1, 1, 2),
            listOf(1, 1, 3),
            listOf(1, 1, 4),
            listOf(1, 2, 2),
            listOf(1, 2, 3),
            listOf(1, 2, 4),
            listOf(1, 3, 3),
            listOf(1, 3, 4),
            listOf(1, 4, 4),
            listOf(2, 2, 2),
            listOf(2, 2, 3),
            listOf(2, 2, 4),
            listOf(2, 3, 3),
            listOf(2, 3, 4),
            listOf(2, 4, 4),
            listOf(3, 3, 3),
            listOf(3, 3, 4),
            listOf(3, 4, 4),
            listOf(4, 4, 4),
        )
    }
})
