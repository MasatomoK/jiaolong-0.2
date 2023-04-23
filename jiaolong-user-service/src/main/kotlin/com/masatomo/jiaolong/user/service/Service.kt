package com.masatomo.jiaolong.user.service

import com.masatomo.jiaolong.core.domain.values.IntegralId
import com.masatomo.jiaolong.user.domain.User

interface UserService {
    suspend fun register(user: User): IntegralId<User>
    suspend fun find(id: IntegralId<User>): User?
}
