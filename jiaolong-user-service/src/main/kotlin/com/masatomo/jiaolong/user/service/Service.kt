package com.masatomo.jiaolong.user.service

import com.masatomo.jiaolong.core.common.IntegralId
import com.masatomo.jiaolong.user.domain.User
import com.masatomo.jiaolong.user.repository.UserRepository


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
