package com.masatomo.jiaolong.core.domain.values

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import kotlin.reflect.full.primaryConstructor


interface LongValue : InlineDomainValue<Long> {

    companion object {
        operator fun invoke(value: Long) = Anonymous(value)

        @JvmInline
        value class Anonymous(override val value: Long) : LongValue
    }
}


inline fun <V : LongValue, reified R : LongValue> V.to(): R = R::class.primaryConstructor!!.call(value)
