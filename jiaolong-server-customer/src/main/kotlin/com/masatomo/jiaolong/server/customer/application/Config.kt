package com.masatomo.jiaolong.server.customer.application

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.authentication
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import org.koin.ktor.plugin.koin

fun Application.module() {
    koin {
        configure()
    }

    logging {
        configure()
    }

    authentication {
        configure()
    }

    routing {
        install(ContentNegotiation) {
            json()
        }
        configure()
    }

    graphQL {
        configure()
    }

    shutdown {
        configure()
    }
}
