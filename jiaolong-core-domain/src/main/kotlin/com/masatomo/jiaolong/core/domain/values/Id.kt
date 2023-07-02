package com.masatomo.jiaolong.core.domain.values


interface Id<E>

interface IntegralId<E> : LongValue, Id<E> {

    companion object {
        operator fun <E> invoke(value: Long): IntegralId<E> = Anonymous(value)
    }

    @JvmInline
    value class Anonymous<E>(override val value: Long) : IntegralId<E>
}

interface StringId<E> : StringValue, Id<E> {
    companion object {
        operator fun <E> invoke(value: String): StringId<E> = Anonymous(value)
    }

    @JvmInline
    value class Anonymous<E>(override val value: String) : StringId<E>
}

typealias Code<E> = StringId<E>
