package com.masatomo.jiaolong.core.repository

import com.masatomo.jiaolong.core.common.Id
import com.masatomo.jiaolong.core.domain.DomainEntity
import kotlin.reflect.KClass


annotation class GenerateRepository(
    vararg val types: KClass<out RepositoryType>
)

interface RepositoryType

interface Repository<E : DomainEntity<E, I>, I : Id<E>> {
    fun register(entity: E): I

    fun findAll(): Iterable<E>
    fun findById(id: I): E?

    fun update(entity: E)

    fun delete(id: I)
}
