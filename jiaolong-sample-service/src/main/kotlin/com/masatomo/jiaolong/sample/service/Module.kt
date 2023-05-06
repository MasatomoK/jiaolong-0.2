package com.masatomo.jiaolong.sample.service

import com.masatomo.jiaolong.sample.service.impl.UserServiceImpl
import org.koin.dsl.module


val sampleServiceModule = module {
    single<UserService> { UserServiceImpl(get()) }
}
