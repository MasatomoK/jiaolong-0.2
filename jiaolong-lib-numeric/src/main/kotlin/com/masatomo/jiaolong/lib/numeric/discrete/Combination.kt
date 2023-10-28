package com.masatomo.jiaolong.lib.numeric.discrete

fun <T : Comparable<T>> orderedCombination(
    length: Int,
    allowDuplication: Boolean,
    vararg elements: T
): Sequence<Sequence<T>> =
    if (length == 1) elements.asSequence().map { sequenceOf(it) }
    else elements.asSequence()
        .flatMap { num -> orderedCombination(length - 1, allowDuplication, *elements).map { num to it } }
        .mapNotNull {
            val secondTarget = it.second.first()
            if (it.first < secondTarget || (allowDuplication && it.first == secondTarget)) sequence {
                yield(it.first)
                yieldAll(it.second)
            } else null
        }

fun <T> combination(length: Int, vararg elements: T): Sequence<Sequence<T>> =
    if (length == 1) elements.asSequence().map { sequenceOf(it) }
    else elements.asSequence().flatMap { num ->
        combination(length - 1, *elements).map {
            sequence {
                yield(num)
                yieldAll(it)
            }
        }
    }
