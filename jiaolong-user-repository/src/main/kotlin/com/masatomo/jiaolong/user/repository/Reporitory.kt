package com.masatomo.jiaolong.user.repository

import com.masatomo.jiaolong.core.common.IntegralId
import com.masatomo.jiaolong.core.repository.Repository
import com.masatomo.jiaolong.user.domain.User


interface UserRepository : Repository<User, IntegralId<User>> {
    override fun register(entity: User): IntegralId<User>
}

internal class UserRepositoryImpl : UserRepository {

    private val entities: MutableMap<IntegralId<User>, User> = mutableMapOf()

    @Synchronized
    override fun register(entity: User): IntegralId<User> =
        takeIf { entity.id == IntegralId.UNASSIGNED }
            ?.let { IntegralId<User>(entities.size.toLong()) }
            ?.also { entities[it] = entity.assigned(it) }
            ?: entity.id

    override fun findAll(): Iterable<User> = entities.values
    override fun findById(id: IntegralId<User>): User? = entities[id]

    override fun update(entity: User) {
        entities[entity.id] = entity
    }

    @Synchronized
    override fun delete(id: IntegralId<User>) {
        entities.remove(id)
    }
}
