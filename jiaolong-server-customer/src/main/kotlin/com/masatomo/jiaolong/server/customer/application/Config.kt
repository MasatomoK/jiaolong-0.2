package com.masatomo.jiaolong.server.customer.application

import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
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
        configure()
    }

    graphQL {
        configure()
    }

    shutdown {
        configure()
    }
}
