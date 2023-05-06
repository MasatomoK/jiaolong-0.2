package com.masatomo.jiaolong.first.server.application

import com.masatomo.jiaolong.core.server.config.configureDefault
import com.masatomo.jiaolong.core.server.config.graphQL
import com.masatomo.jiaolong.core.server.config.logging
import com.masatomo.jiaolong.core.server.config.registerModules
import com.masatomo.jiaolong.core.server.config.shutdown
import com.masatomo.jiaolong.core.server.definition.ApplicationDefinition
import com.masatomo.jiaolong.first.server.domain.hello.helloEntryPoint
import com.masatomo.jiaolong.first.server.domain.user.userEntryPoint
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.routing.routing
import org.koin.ktor.plugin.koin


fun Application.module() {

    val def = ApplicationDefinition(
        helloEntryPoint,
        userEntryPoint
    )

    koin {
        registerModules(def)
    }

    logging {
        configureDefault()
    }

    authentication {
        configureDefault()
    }

    routing {
        configureDefault(def)
    }

    graphQL {
        configureDefault(def)
    }

    shutdown {
        configureDefault()
    }
}
