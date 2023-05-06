package com.masatomo.jiaolong.sample.repository

import com.masatomo.jiaolong.sample.repository.impl.ExposedUserRepositoryImpl
import org.koin.dsl.module


val sampleRepositoryModule = module {
//    single<UserRepository> { UserRepositoryImpl() }
    single<UserRepository> { ExposedUserRepositoryImpl() }
}
