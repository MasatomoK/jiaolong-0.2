package com.masatomo.jiaolong.user.repository

import org.koin.dsl.module


val userRepositoryModule = module {
    single<UserRepository> { UserRepositoryImpl() }
}
