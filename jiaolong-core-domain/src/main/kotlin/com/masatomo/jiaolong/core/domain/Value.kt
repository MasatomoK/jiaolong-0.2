package com.masatomo.jiaolong.core.domain

import com.masatomo.jiaolong.core.validation.InvalidDomainValue

interface DomainValue {
    fun isValid(): Collection<InvalidDomainValue>
}

interface InlineDomainValue<out T> : DomainValue {
    val value: T
}
