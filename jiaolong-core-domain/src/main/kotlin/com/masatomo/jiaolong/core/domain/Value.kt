package com.masatomo.jiaolong.core.domain

import com.masatomo.jiaolong.core.validation.AnnotatedValueValidator
import com.masatomo.jiaolong.core.validation.CustomValueValidator
import com.masatomo.jiaolong.core.validation.InvalidDomainValue
import com.masatomo.jiaolong.core.validation.validateBy


interface DomainValue<V : DomainValue<V>> {
    val annotatedValidators: Array<AnnotatedValueValidator<V>>
    val customValidators: Array<CustomValueValidator<V>> get() = emptyArray()
    fun isValid(): Collection<InvalidDomainValue<V>> = validateBy(annotatedValidators, customValidators)
}

interface InlineDomainValue<out T, V : DomainValue<V>> : DomainValue<V> {
    val value: T
}
