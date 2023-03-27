package com.masatomo.jiaolong.user.service

import org.koin.dsl.module


val userServiceModule = module {
    single<UserService> { UserServiceImpl(get()) }
}
