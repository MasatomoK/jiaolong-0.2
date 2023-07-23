package com.masatomo.jiaolong.core.domain

import com.masatomo.jiaolong.core.domain.values.Id
import java.io.Serializable

interface DomainValue : Serializable

interface InlineDomainValue<out T> : DomainValue {
    val value: T
}

interface IdentifiedValue<V : IdentifiedValue<V, I>, I : Id<V>> : DomainValue {
    val id: I
}
