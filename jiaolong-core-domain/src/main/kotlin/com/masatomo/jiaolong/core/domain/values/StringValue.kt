package com.masatomo.jiaolong.core.domain.values

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import kotlin.reflect.full.primaryConstructor


interface StringValue : InlineDomainValue<String> {

    companion object {
        operator fun invoke(value: String) = Anonymous(value)

        @JvmInline
        value class Anonymous(override val value: String) : StringValue
    }
}

inline fun <V : StringValue, reified R : StringValue> V.to(): R = R::class.primaryConstructor!!.call(value)

@JvmInline
value class Name<V>(override val value: String) : StringValue {
    init {
        // TODO: validation
    }
}
