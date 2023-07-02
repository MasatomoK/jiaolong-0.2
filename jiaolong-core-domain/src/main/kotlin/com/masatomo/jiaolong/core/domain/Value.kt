package com.masatomo.jiaolong.core.domain

import java.io.Serializable

interface DomainValue : Serializable

interface InlineDomainValue<out T> : DomainValue {
    val value: T
}
