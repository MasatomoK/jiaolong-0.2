package com.masatomo.jiaolong.core.domain.values

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import java.math.BigDecimal
import kotlin.reflect.full.primaryConstructor


interface BigDecimalValue : InlineDomainValue<BigDecimal> {

    companion object {
        operator fun invoke(value: BigDecimal) = Anonymous(value)

        @JvmInline
        value class Anonymous(override val value: BigDecimal) : BigDecimalValue
    }
}

inline fun <V : BigDecimalValue, reified R : BigDecimalValue> V.to(): R = R::class.primaryConstructor!!.call(value)
