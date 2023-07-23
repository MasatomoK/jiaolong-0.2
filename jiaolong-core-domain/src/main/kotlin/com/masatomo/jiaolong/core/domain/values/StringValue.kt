package com.masatomo.jiaolong.core.domain.values

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import com.masatomo.jiaolong.core.validation.validate
import com.masatomo.jiaolong.core.validation.values.NameConstraint
import com.masatomo.jiaolong.core.validation.values.maximumLength
import com.masatomo.jiaolong.core.validation.values.minimumLength
import kotlin.reflect.full.findAnnotations
import kotlin.reflect.full.primaryConstructor


interface StringValue : InlineDomainValue<String> {

    companion object {
        operator fun invoke(value: String) = Anonymous(value)
    }

    @JvmInline
    value class Anonymous(override val value: String) : StringValue
}

inline fun <V : StringValue, reified R : StringValue> V.to(): R = R::class.primaryConstructor!!.call(value)

interface Name<T> : StringValue {
    @JvmInline
    value class Anonymous<T>(override val value: String) : Name<T>
}

inline fun <reified T> Name(value: String): Name<T> = Name.Anonymous<T>(value).apply {
    validate {
        T::class.findAnnotations(NameConstraint.MaxLength::class)
            .forEach { maximumLength(it.limit) }
        T::class.findAnnotations(NameConstraint.MinLength::class)
            .forEach { minimumLength(it.limit) }
    }
}
