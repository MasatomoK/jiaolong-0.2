package com.masatomo.jiaolong.sample.service

import com.masatomo.jiaolong.core.domain.values.StringId
import com.masatomo.jiaolong.core.repository.TransactionScope
import com.masatomo.jiaolong.sample.domain.User

interface UserService {
    suspend fun register(user: User): User.Id
    suspend fun find(id: User.Id): User?
}

object UserTransactionScope : TransactionScope {
    override val id: StringId<TransactionScope> = StringId("user")
}
