package com.masatomo.jiaolong.core.server.config

import com.masatomo.jiaolong.core.server.definition.ApplicationDefinition
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.openapi.openAPI
import io.ktor.server.plugins.swagger.swaggerUI
import io.ktor.server.routing.Route

fun Route.configureDefault(definition: ApplicationDefinition) {
    install(ContentNegotiation) {
        json()
    }
    definition.route.forEach { it.invoke(this) }

    openAPI(path = "openapi", swaggerFile = "openapi/documentation.yaml")
    swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml")
}
