package com.masatomo.jiaolong.user.service

import com.masatomo.jiaolong.core.domain.values.IntegralId
import com.masatomo.jiaolong.core.domain.values.StringId
import com.masatomo.jiaolong.core.repository.TransactionScope
import com.masatomo.jiaolong.user.domain.User

interface UserService {
    suspend fun register(user: User): IntegralId<User>
    suspend fun find(id: IntegralId<User>): User?
}

object UserTransactionScope : TransactionScope {
    override val id: StringId<TransactionScope> = StringId("user")
}
