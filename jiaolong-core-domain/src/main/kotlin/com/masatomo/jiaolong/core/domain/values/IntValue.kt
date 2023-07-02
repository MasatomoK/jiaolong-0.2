package com.masatomo.jiaolong.core.domain.values

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import kotlin.reflect.full.primaryConstructor


interface IntValue : InlineDomainValue<Int> {

    companion object {
        operator fun invoke(value: Int) = Anonymous(value)

        @JvmInline
        value class Anonymous(override val value: Int) : IntValue
    }
}

inline fun <V : IntValue, reified R : IntValue> V.to(): R = R::class.primaryConstructor!!.call(value)
