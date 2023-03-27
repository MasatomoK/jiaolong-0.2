package com.masatomo.jiaolong.core.common


interface Id<E>

@JvmInline
value class IntegralId<E>(override val value: Long) : LongValue, Id<E> {
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <E> unassigned() = UNASSIGNED as IntegralId<E>
        val UNASSIGNED = IntegralId<Nothing>(Long.MIN_VALUE)
    }
}

@JvmInline
value class StringId<E>(override val value: String) : StringValue, Id<E> {
    companion object {
        @Suppress("UNCHECKED_CAST")
        fun <E> unassigned() = UNASSIGNED as StringId<E>
        val UNASSIGNED = StringId<Nothing>("*".repeat(100))
    }
}
