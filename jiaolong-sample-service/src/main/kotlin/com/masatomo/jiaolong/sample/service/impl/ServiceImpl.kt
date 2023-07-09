package com.masatomo.jiaolong.sample.service.impl

import com.masatomo.jiaolong.sample.domain.User
import com.masatomo.jiaolong.sample.repository.UserRepository
import com.masatomo.jiaolong.sample.service.UserService


internal class UserServiceImpl(
    private val repository: UserRepository
) : UserService {
    override suspend fun register(user: User): User.Id {
        return repository.register(user)
    }

    override suspend fun find(id: User.Id): User? = repository.findById(id)
}
