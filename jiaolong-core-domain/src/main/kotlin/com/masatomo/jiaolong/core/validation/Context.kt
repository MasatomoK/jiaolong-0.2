package com.masatomo.jiaolong.core.validation

import com.masatomo.jiaolong.core.domain.ApplicationRuntimeException
import com.masatomo.jiaolong.core.domain.DomainEntity
import com.masatomo.jiaolong.core.domain.DomainValue
import com.masatomo.jiaolong.core.domain.values.Code
import kotlin.reflect.KClass

interface InvalidDomainValue : InvalidReason {
    val kClass: KClass<out DomainValue>
}

interface InvalidDomainEntity {
    val kClass: KClass<out DomainEntity<*, *>>
}


class ValidationContext<T : Any>(val target: T) {

    private val reasons = mutableListOf<InvalidReason>()

    fun addReason(reason: InvalidReason) {
        reasons.add(reason)
    }

    fun throwExceptionIfInvalid() {
        if (reasons.isEmpty()) {
            return
        }
        throw InvalidValidationException("XXX", target::class, reasons)
    }
}

interface InvalidReason {
    val code: Code<InvalidReason>
        get() = Code(this::class.java.simpleName)
}

class InvalidValidationException(code: String, val kClass: KClass<*>, val reasons: List<InvalidReason>) :
    ApplicationRuntimeException(code)

fun <V : Any> V.validate(target: V, block: ValidationContext<V>.() -> Unit) {
    ValidationContext(target).apply(block).throwExceptionIfInvalid()
}
