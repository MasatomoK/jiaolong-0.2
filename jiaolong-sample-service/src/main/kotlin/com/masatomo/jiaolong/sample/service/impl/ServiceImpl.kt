package com.masatomo.jiaolong.sample.service.impl

import com.masatomo.jiaolong.core.domain.values.IntegralId
import com.masatomo.jiaolong.sample.domain.User
import com.masatomo.jiaolong.sample.repository.UserRepository
import com.masatomo.jiaolong.sample.service.UserService


internal class UserServiceImpl(
    private val repository: UserRepository
) : UserService {
    override suspend fun register(user: User): IntegralId<User> {
        val validations = user.isValid()
        if (validations.isNotEmpty()) {
            throw RuntimeException(validations.toString()) // TODO
        }
        return repository.register(user)
    }

    override suspend fun find(id: IntegralId<User>): User? = repository.findById(id)
}
