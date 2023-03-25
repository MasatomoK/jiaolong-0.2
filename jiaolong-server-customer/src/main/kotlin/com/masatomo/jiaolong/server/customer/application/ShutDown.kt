package com.masatomo.jiaolong.server.customer.application

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.ShutDownUrl
import io.ktor.server.response.respondText
import kotlinx.coroutines.runBlocking


internal fun Application.shutdown(configure: ShutDownUrl.Config.() -> Unit) {
    install(ShutDownUrl.ApplicationCallPlugin) {
        configure()
    }
}

internal fun ShutDownUrl.Config.configure() {
    shutDownUrl = "/shutdown"
    exitCodeSupplier = {
        runBlocking {
            respondText("Shutdown Application!")
            0
        }
    }
}
