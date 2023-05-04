package com.masatomo.jiaolong.user.repository

import com.masatomo.jiaolong.user.repository.impl.ExposedUserRepositoryImpl
import org.koin.dsl.module


val userRepositoryModule = module {
//    single<UserRepository> { UserRepositoryImpl() }
    single<UserRepository> { ExposedUserRepositoryImpl() }
}
