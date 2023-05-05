package com.masatomo.jiaolong.server.customer.domain.user

import com.masatomo.jiaolong.core.server.definition.entryPoint
import com.masatomo.jiaolong.core.server.modulesIn
import io.ktor.server.routing.Route


internal val userEntryPoint = entryPoint {
    module {
        includes(
            *modulesIn("com.masatomo.jiaolong.user.service.grpc.ModuleKt")
        )
    }
    graphQL {
    }
    route(Route::configureUser)
}
