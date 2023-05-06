package com.masatomo.jiaolong.sample.repository

import com.masatomo.jiaolong.core.domain.values.IntegralId
import com.masatomo.jiaolong.core.repository.Repository
import com.masatomo.jiaolong.core.repository.exposed.ksp.GenerateAbstractExposedRepository
import com.masatomo.jiaolong.core.repository.ksp.GenerateAbstractOnMemoryRepository
import com.masatomo.jiaolong.sample.domain.User


@GenerateAbstractOnMemoryRepository
@GenerateAbstractExposedRepository
interface UserRepository : Repository<User, IntegralId<User>> {
    fun sampleMethod()
}
