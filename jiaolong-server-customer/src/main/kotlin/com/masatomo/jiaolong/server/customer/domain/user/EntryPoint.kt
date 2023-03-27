package com.masatomo.jiaolong.server.customer.domain.user

import com.masatomo.jiaolong.core.server.definition.entryPoint
import com.masatomo.jiaolong.user.repository.userRepositoryModule
import com.masatomo.jiaolong.user.service.userServiceModule
import io.ktor.server.routing.Route


internal val userEntryPoint = entryPoint {
    module {
        includes(
            userRepositoryModule,
            userServiceModule,
        )
    }
    graphQL {
    }
    route(Route::configureUser)
}
