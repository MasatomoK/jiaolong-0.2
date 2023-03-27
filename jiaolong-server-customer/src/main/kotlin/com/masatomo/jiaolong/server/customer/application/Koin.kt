package com.masatomo.jiaolong.server.customer.application

import com.masatomo.jiaolong.server.customer.domain.user.userModules
import org.koin.core.KoinApplication


fun KoinApplication.configure() {
    modules(
        userModules
    )
}
