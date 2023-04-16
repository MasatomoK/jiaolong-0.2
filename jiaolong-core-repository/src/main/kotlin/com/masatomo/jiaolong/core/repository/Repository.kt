package com.masatomo.jiaolong.core.repository

import com.masatomo.jiaolong.core.common.Id
import com.masatomo.jiaolong.core.domain.DomainEntity


interface Repository<E : DomainEntity<E, I>, I : Id<E>> {
    fun register(entity: E): I

    fun findAll(): Iterable<E>
    fun findById(id: I): E?

    fun update(entity: E)

    fun delete(id: I)
}

data class Paging(
    val perPage: Int,
    val page: Int,
)
