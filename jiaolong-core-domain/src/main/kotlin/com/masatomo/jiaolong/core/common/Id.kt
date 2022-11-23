package com.masatomo.jiaolong.core.common


interface Id<E>

interface IntegralId<E> : LongValue, Id<E> {
    companion object {
        operator fun <E> invoke(value: Long) = Anonymous<E>(value)
        data class Anonymous<E>(override val value: Long) : IntegralId<E>
    }
}

interface StringId<E> : StringValue, Id<E> {
    companion object {
        operator fun <E> invoke(value: String) = Anonymous<E>(value)
        data class Anonymous<E>(override val value: String) : StringId<E>
    }
}
