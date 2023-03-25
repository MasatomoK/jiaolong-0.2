package com.masatomo.jiaolong.server.customer.application

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.plugins.callloging.CallLoggingConfig
import io.ktor.server.request.path
import org.slf4j.event.Level

internal fun Application.logging(configure: CallLoggingConfig.() -> Unit) {
    install(CallLogging) {
        configure()
    }
}

internal fun CallLoggingConfig.configure() {
    level = Level.INFO
    filter { call -> call.request.path().startsWith("/") }
}
