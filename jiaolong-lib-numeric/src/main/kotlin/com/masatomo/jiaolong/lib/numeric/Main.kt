package com.masatomo.jiaolong.lib.numeric

import com.masatomo.jiaolong.lib.numeric.algebra.BinaryNode
import com.masatomo.jiaolong.lib.numeric.algebra.Rational
import com.masatomo.jiaolong.lib.numeric.algebra.RationalOperator
import com.masatomo.jiaolong.lib.numeric.discrete.orderedCombination

class TenPuzzleSolver(
    private val shouldBe: Int = 10,
    private val operators: Set<RationalOperator> = setOf(*RationalOperator.values()),
) {
    fun solveOneResult(vararg from: Int) =
        generateNodes(*from).filter { it.value == Rational(shouldBe) }.firstOrNull()

    fun solveAllResults(vararg from: Int) =
        generateNodes(*from).filter { it.value == Rational(shouldBe) }.distinct()

    private fun generateNodes(vararg numbers: Int): Sequence<BinaryNode<Rational>> = when (numbers.size) {
        1 -> sequenceOf(BinaryNode.of(Rational(numbers.first())))
        else -> (1..numbers.size / 2).asSequence()
            .flatMap { orderedCombination(it, false, *numbers.indices.toList().toTypedArray()) }
            .map { leftIndexes ->
                val leftNumbers = numbers.filterIndexed { index, _ -> leftIndexes.contains(index) }
                val rightNumbers = numbers.filterIndexed { index, _ -> !leftIndexes.contains(index) }
                leftNumbers to rightNumbers
            }.filter { (leftNumbers, rightNumbers) ->
                if (leftNumbers.size > rightNumbers.size) return@filter false
                if (leftNumbers.size < rightNumbers.size) return@filter true
                leftNumbers.forEachIndexed { index, l ->
                    if (l < rightNumbers[index]) return@filter true
                    if (l > rightNumbers[index]) return@filter false
                }
                true
            }
            .flatMap { (leftNumbers, rightNumbers) ->
                val leftNodes = generateNodes(*leftNumbers.toIntArray())
                val rightNodes = generateNodes(*rightNumbers.toIntArray())
                leftNodes.flatMap { left -> rightNodes.map { right -> left to right } }
                    .flatMap {
                        operators.asSequence()
                            .map { ope -> BinaryNode.of(it.first, it.second, ope) }
                            .filter { it.isValid }
                    }
            }
    }

    private val BinaryNode<Rational>.isValid
        get() = when (this) {
            is BinaryNode.Internal -> when (ope) {
                RationalOperator.CONCAT ->
                    first is BinaryNode.Leaf && second is BinaryNode.Leaf && first.value != Rational(0)

                RationalOperator.LCONCAT ->
                    first is BinaryNode.Leaf && second is BinaryNode.Leaf && second.value != Rational(0)

                else -> true
            }

            else -> true
        }
}

fun main() {
    // 利用できる演算子を列挙する。
    val targetOperators = setOf(
        RationalOperator.ADD,
        RationalOperator.SUB,
        RationalOperator.LSUB,
        RationalOperator.MULTI,
        RationalOperator.DIV,
        RationalOperator.LDIV,
        RationalOperator.POW,
        RationalOperator.LPOW,
        RationalOperator.CONCAT,
        RationalOperator.LCONCAT,
    )
    val solver = TenPuzzleSolver(shouldBe = 10, operators = targetOperators)

    // 答えを一つ求める場合
    solver.solveOneResult(5, 8, 9, 9).let { println(it) }

    // すべての答えを求める場合
    solver.solveAllResults(1, 2, 4, 5).forEach { println(it) }

    // 4つの数字の組み合わせの解答を全て洗い出す場合
    orderedCombination(4, true, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9).forEach { seq ->
        solver.solveAllResults(*seq.toList().toIntArray())
            .map { "[${seq.joinToString("-")}]: $it = ${it.value}" }
            .distinct()
            .forEach { println(it) }
    }

    // 1つでも解の存在する組み合わせを洗い出す場合
    orderedCombination(6, true, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9).forEach { seq ->
        solver.solveOneResult(*seq.toList().toIntArray())
            ?.let { "[${seq.joinToString("-")}]: $it = ${it.value}" }
            ?.let { println(it) }
    }
}
