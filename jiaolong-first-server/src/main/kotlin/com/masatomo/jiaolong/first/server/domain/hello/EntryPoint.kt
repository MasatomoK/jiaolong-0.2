package com.masatomo.jiaolong.first.server.domain.hello

import com.masatomo.jiaolong.core.server.definition.entryPoint
import io.ktor.server.routing.Route


internal val helloEntryPoint = entryPoint {
    module { }
    graphQL {
        `package`("com.masatomo.jiaolong.first.server.domain.hello")
        query(HelloQuery)
        mutation(HelloMutation)
    }
    route(Route::configureHello)
}
