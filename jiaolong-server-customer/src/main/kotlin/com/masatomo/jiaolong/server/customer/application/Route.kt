package com.masatomo.jiaolong.server.customer.application

import com.masatomo.jiaolong.server.customer.domain.hello.configureHello
import com.masatomo.jiaolong.server.customer.domain.user.configureUser
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.Route

fun Route.configure() {

    configureHello()
    configureUser()

    openAPI(path = "openapi", swaggerFile = "openapi/documentation.yaml")
    swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
}
