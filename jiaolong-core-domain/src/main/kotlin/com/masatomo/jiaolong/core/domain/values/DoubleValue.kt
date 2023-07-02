package com.masatomo.jiaolong.core.domain.values

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import kotlin.reflect.full.primaryConstructor


interface DoubleValue : InlineDomainValue<Double> {

    companion object {
        operator fun invoke(value: Double) = Anonymous(value)

        @JvmInline
        value class Anonymous(override val value: Double) : DoubleValue
    }
}


inline fun <V : DoubleValue, reified R : DoubleValue> V.to(): R = R::class.primaryConstructor!!.call(value)
