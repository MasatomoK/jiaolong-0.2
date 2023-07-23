package com.masatomo.jiaolong.core.domain.values

import com.masatomo.jiaolong.core.validation.validate
import com.masatomo.jiaolong.core.validation.values.CodeConstraint
import com.masatomo.jiaolong.core.validation.values.maximumLength
import com.masatomo.jiaolong.core.validation.values.minimumLength
import kotlin.reflect.full.findAnnotations


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

inline fun <reified T> Code(value: String): Code<T> = StringId<T>(value).apply {
    validate {
        T::class.findAnnotations(CodeConstraint.MaxLength::class)
            .forEach { maximumLength(it.limit) }
        T::class.findAnnotations(CodeConstraint.MinLength::class)
            .forEach { minimumLength(it.limit) }
    }
}
