package com.masatomo.jiaolong.core.server.config

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.engine.ShutDownUrl
import io.ktor.server.response.respondText
import kotlinx.coroutines.runBlocking

fun Application.shutdown(configure: ShutDownUrl.Config.() -> Unit) {
    install(ShutDownUrl.ApplicationCallPlugin) {
        configure()
    }
}

fun ShutDownUrl.Config.configureDefault() {
    shutDownUrl = "/shutdown"
    exitCodeSupplier = {
        runBlocking {
            respondText("Shutdown Application!")
            0
        }
    }
}
