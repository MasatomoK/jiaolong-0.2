package com.masatomo.jiaolong.admin.service

import com.masatomo.jiaolong.admin.service.impl.AdminServiceImpl
import org.koin.dsl.module


val adminServiceModule = module {
    single<AdminService> { AdminServiceImpl(get()) }
}
