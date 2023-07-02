package com.masatomo.jiaolong.core.domain.values

import com.masatomo.jiaolong.core.domain.InlineDomainValue
import java.time.LocalDateTime
import kotlin.reflect.full.primaryConstructor


interface LocalDateTimeValue : InlineDomainValue<LocalDateTime> {

    companion object {
        operator fun invoke(value: LocalDateTime) = Anonymous(value)

        @JvmInline
        value class Anonymous(override val value: LocalDateTime) : LocalDateTimeValue
    }
}

inline fun <V : LocalDateTimeValue, reified R : LocalDateTimeValue> V.to(): R =
    R::class.primaryConstructor!!.call(value)
