package com.masatomo.jiaolong.server.customer.domain.hello

import io.ktor.server.application.call
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get


fun Route.configureHello() {
    get("/hello") {
        call.respondText("Hello")
    }

    get("/healthcheck") {
        call.respondText("OK!")
    }
}
