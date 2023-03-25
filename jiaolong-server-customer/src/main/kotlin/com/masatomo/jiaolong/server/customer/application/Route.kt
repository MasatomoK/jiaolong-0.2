package com.masatomo.jiaolong.server.customer.application

import io.ktor.server.application.call
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.configure() {
    get("/hello") {
        call.respondText("Hello")
    }

    get("/healthcheck") {
        call.respondText("OK!")
    }

    openAPI(path = "openapi", swaggerFile = "openapi/documentation.yaml")
    swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
}
