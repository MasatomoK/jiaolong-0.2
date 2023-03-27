package com.masatomo.jiaolong.user.service

import com.masatomo.jiaolong.core.common.IntegralId
import com.masatomo.jiaolong.user.domain.User
import com.masatomo.jiaolong.user.repository.UserRepository


interface UserService {
    fun register(user: User): IntegralId<User>
    fun find(id: IntegralId<User>): User?
}

internal class UserServiceImpl(
    private val repository: UserRepository
) : UserService {
    override fun register(user: User): IntegralId<User> {
        val validations = user.isValid()
        if (validations.isNotEmpty()) {
            throw RuntimeException(validations.toString()) // TODO
        }
        return repository.register(user)
    }

    override fun find(id: IntegralId<User>): User? = repository.findById(id)
}
