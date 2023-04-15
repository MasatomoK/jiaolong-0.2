package com.masatomo.jiaolong.user.repository

import com.masatomo.jiaolong.core.common.IntegralId
import com.masatomo.jiaolong.core.repository.GenerateRepository
import com.masatomo.jiaolong.core.repository.Repository
import com.masatomo.jiaolong.core.repository.ksp.OnMemory
import com.masatomo.jiaolong.user.domain.User


@GenerateRepository(OnMemory::class)
interface UserRepository : Repository<User, IntegralId<User>> {
    fun sampleMethod()
}


class UserRepositoryImpl : AbstractOnMemoryUserRepository() {
    override fun sampleMethod() {
        TODO("Not yet implemented")
    }
}
