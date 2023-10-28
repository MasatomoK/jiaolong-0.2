package com.masatomo.jiaolong.lib.numeric.algebra

data class Rational(val numerator: Int, val denominator: Int) : Body<Rational> {
    constructor(number: Int) : this(number, 1)

    private val gcb by lazy { gcb(numerator, denominator) }
    val isValid get() = denominator != 0

    override fun toString(): String = if (denominator == 1) "$numerator" else "$numerator/$denominator"
    override fun equals(other: Any?): Boolean {
        if (other == null) return false
        if (other !is Rational) return false
        val reducedThis = reduce()
        val reducedOther = other.reduce()
        return reducedThis.numerator == reducedOther.numerator && reducedThis.denominator == reducedOther.denominator
    }

    override fun hashCode(): Int = reduce().run {
        numerator.let { 31 * it + denominator }
    }

    override operator fun plus(other: Rational): Rational = Rational(
        this.numerator * other.denominator + other.numerator * this.denominator,
        this.denominator * other.denominator
    ).reduce()

    override operator fun minus(other: Rational): Rational = Rational(
        this.numerator * other.denominator - other.numerator * this.denominator,
        this.denominator * other.denominator
    ).reduce()

    override operator fun times(other: Rational): Rational = Rational(
        this.numerator * other.numerator, this.denominator * other.denominator
    ).reduce()

    override operator fun div(other: Rational): Rational = Rational(
        this.numerator * other.denominator, this.denominator * other.numerator
    ).reduce()

    fun pow(other: Rational): Rational = when {
        this.numerator == this.denominator -> Rational(1, 0)
        this.numerator == 0 -> Rational(1, 0)
        other.numerator == 0 -> Rational(1, 0)
        other.denominator != 1 -> Rational(0, 0)
        other.numerator > 32 -> Rational(0, 0) // 計算が爆発することを防ぐため必要以上のべき乗を実施しない。(2^32 = MAX_INT)
        other.numerator < -32 -> Rational(0, 0) // 計算が爆発することを防ぐため必要以上のべき乗を実施しない。(2^32 = MAX_INT)
        other.numerator < 0 -> Rational(
            (other.numerator..-1).map { denominator }.reduce { a, b -> a * b },
            (other.numerator..-1).map { numerator }.reduce { a, b -> a * b },
        )

        else -> Rational(
            (1..other.numerator).map { numerator }.reduce { a, b -> a * b },
            (1..other.numerator).map { denominator }.reduce { a, b -> a * b },
        )
    }.reduce()

    fun reduce(): Rational = when (gcb) {
        0, 1 -> this
        else -> Rational(numerator / gcb, denominator / gcb)
    }
}

private fun gcb(a: Int, b: Int): Int = if (b == 0) a else gcb(b, a % b)

enum class RationalOperator(
    private val sign: String,
    private val operation: (Rational, Rational) -> Rational,
    private val inverse: Boolean,
    private val strength: Int,
) : Operator<Rational> {
    ADD(" + ", Rational::plus, false, 1),
    SUB(" - ", Rational::minus, false, 1),
    LSUB(" - ", { a, b -> b - a }, true, 1),
    MULTI(" * ", Rational::times, false, 2),
    DIV(" / ", { a, b -> a / b }, false, 2),
    LDIV(" / ", { a, b -> b / a }, true, 2),
    POW(" ^ ", Rational::pow, false, 3),
    LPOW(" ^ ", { a, b -> b.pow(a) }, true, 3),
    CONCAT("", { a, b -> a * Rational(10) + b }, false, 3),
    LCONCAT("", { a, b -> b * Rational(10) + a }, true, 3),
    ;

    override fun invoke(first: Rational, second: Rational): Rational = operation(first, second)
    override fun toString(
        firstValue: String,
        firstOperator: Operator<Rational>?,
        secondValue: String,
        secondOperator: Operator<Rational>?
    ): String {
        return if (inverse) {
            "${format(secondValue, secondOperator)}${sign}${format(firstValue, firstOperator, true)}"
        } else {
            "${format(firstValue, firstOperator)}${sign}${format(secondValue, secondOperator, true)}"
        }
    }

    private fun format(childValue: String, childOperator: Operator<Rational>?, isSecond: Boolean = false): String =
        if (childOperator is RationalOperator) when {
            strength > childOperator.strength -> "($childValue)"
            strength == childOperator.strength && isSecond -> "($childValue)"
            else -> childValue
        } else childValue
}
