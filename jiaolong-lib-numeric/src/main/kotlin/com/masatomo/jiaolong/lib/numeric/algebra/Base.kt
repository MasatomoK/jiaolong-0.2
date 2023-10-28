package com.masatomo.jiaolong.lib.numeric.algebra

interface Group<G : Group<G>> {
    operator fun plus(other: G): G
    operator fun minus(other: G): G
}

interface Ring<R : Ring<R>> : Group<R> {
    operator fun times(other: R): R
}

interface Body<B : Body<B>> : Ring<B> {
    operator fun div(other: B): B
}

interface Operator<G> {
    operator fun invoke(first: G, second: G): G
    fun toString(
        firstValue: String,
        firstOperator: Operator<G>?,
        secondValue: String,
        secondOperator: Operator<G>?,
    ): String
}

sealed class BinaryNode<G : Group<G>> {
    abstract val value: G

    companion object {
        fun <G : Group<G>> of(number: G) = Leaf(number)
        fun <G : Group<G>> of(first: BinaryNode<G>, second: BinaryNode<G>, ope: Operator<G>) =
            Internal(first, second, ope)
    }

    data class Leaf<G : Group<G>> internal constructor(val number: G) : BinaryNode<G>() {
        override val value = number
        override fun toString(): String = "$number"
    }

    data class Internal<G : Group<G>> internal constructor(
        val first: BinaryNode<G>,
        val second: BinaryNode<G>,
        val ope: Operator<G>
    ) : BinaryNode<G>() {
        override val value by lazy { ope(first.value, second.value) }

        override fun toString(): String = ope.toString(
            first.toString(), if (first is Internal) first.ope else null,
            second.toString(), if (second is Internal) second.ope else null
        )
    }
}
