package com.masatomo.jiaolong.core.domain.values

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import java.time.LocalDate
import kotlin.reflect.full.primaryConstructor


interface LocalDateValue : InlineDomainValue<LocalDate> {

    companion object {
        operator fun invoke(value: LocalDate) = Anonymous(value)

        @JvmInline
        value class Anonymous(override val value: LocalDate) : LocalDateValue
    }
}

inline fun <V : LocalDateValue, reified R : LocalDateValue> V.to(): R = R::class.primaryConstructor!!.call(value)
