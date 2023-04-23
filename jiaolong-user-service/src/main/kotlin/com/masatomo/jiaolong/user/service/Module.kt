package com.masatomo.jiaolong.user.service

import com.masatomo.jiaolong.user.service.impl.UserServiceImpl
import org.koin.dsl.module


val userServiceModule = module {
    single<UserService> { UserServiceImpl(get()) }
}
