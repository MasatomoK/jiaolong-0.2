package com.masatomo.jiaolong.server.customer.domain.user

import com.masatomo.jiaolong.core.server.definition.entryPoint
import com.masatomo.jiaolong.user.service.userServiceClientModule
import io.ktor.server.routing.Route


internal val userEntryPoint = entryPoint {
    module {
        includes(
            userServiceClientModule,
        )
    }
    graphQL {
    }
    route(Route::configureUser)
}
