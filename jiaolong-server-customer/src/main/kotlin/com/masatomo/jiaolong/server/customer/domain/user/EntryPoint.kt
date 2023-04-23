package com.masatomo.jiaolong.server.customer.domain.user

import com.masatomo.jiaolong.core.server.definition.entryPoint
import com.masatomo.jiaolong.user.service.grpc.userServiceGrpcClientModule
import io.ktor.server.routing.Route


internal val userEntryPoint = entryPoint {
    module {
        includes(
            userServiceGrpcClientModule,
        )
    }
    graphQL {
    }
    route(Route::configureUser)
}
